/**
 * SSH WebSocket 클라이언트
 * SSH 터미널과 실시간 통신하는 WebSocket 클래스
 */
class SSHWebSocketClient {
    constructor() {
        this.ws = null;
        this.sessionId = null;
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectInterval = 2000; // 2초
        this.isConnected = false;
        this.isConnecting = false;
    }

    /**
     * WebSocket 연결
     * @param {string} sessionId
     */
    connect(sessionId) {
        if (this.isConnecting || this.isConnected) {
            console.warn('WebSocket이 이미 연결 중이거나 연결되어 있습니다.');
            return;
        }

        this.sessionId = sessionId;
        this.isConnecting = true;

        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const host = window.location.host; // 현재 페이지와 같은 호스트:포트 사용
        const wsUrl = `${protocol}//${host}/ws/ssh/${sessionId}`;

        console.log('WebSocket 연결 시도:', wsUrl);

        try {
            this.ws = new WebSocket(wsUrl);
            this.setupEventHandlers();
        } catch (error) {
            console.error('WebSocket 연결 생성 실패:', error);
            this.isConnecting = false;
            this.updateStatus('error', '연결 생성 실패');
        }
    }

    /**
     * WebSocket 이벤트 핸들러 설정
     */
    setupEventHandlers() {
        this.ws.onopen = () => {
            console.log('WebSocket 연결됨');
            this.isConnected = true;
            this.isConnecting = false;
            this.reconnectAttempts = 0;
            this.updateStatus('connected', '연결됨');

            // 연결 성공 시 welcome 메시지
            if (window.terminal) {
                window.terminal.write('\r\n\x1b[32m✓ SSH 터미널에 연결되었습니다.\x1b[0m\r\n');
            }
        };

        this.ws.onmessage = (event) => {
            if (window.terminal && event.data) {
                try {
                    // JSON 메시지 확인
                    const parsedData = JSON.parse(event.data);
                    if (parsedData.type === 'resize_response') {
                        console.log('터미널 크기 변경 완료:', parsedData.status);
                        return;
                    }
                } catch (e) {
                    // JSON이 아닌 터미널 출력
                }

                // 데이터 검증 및 처리
                let outputData = event.data;

                // 빈 데이터 필터링
                if (!outputData || outputData.length === 0) {
                    return;
                }

                // 제어 문자 정규화
                outputData = this.normalizeControlCharacters(outputData);

                // 터미널에 출력
                window.terminal.write(outputData);
            }
        };

        this.ws.onclose = (event) => {
            console.log('WebSocket 연결 종료:', event.code, event.reason);
            this.isConnected = false;
            this.isConnecting = false;

            if (event.code === 1000) {
                // 정상 종료
                this.updateStatus('disconnected', '연결 해제됨');
            } else {
                // 비정상 종료 - 재연결 시도
                this.updateStatus('error', '연결 끊김');
                this.attemptReconnect();
            }
        };

        this.ws.onerror = (error) => {
            console.error('WebSocket 오류:', error);
            this.isConnected = false;
            this.isConnecting = false;
            this.updateStatus('error', '연결 오류');
        };
    }

    /**
     * 데이터 전송
     * @param {string} data
     */
    send(data) {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            try {
                this.ws.send(data);
            } catch (error) {
                console.error('WebSocket 전송 오류:', error);
                this.updateStatus('error', '전송 실패');
            }
        } else {
            console.warn('WebSocket이 연결되지 않았습니다. 메시지 전송 실패:', data);
        }
    }

    /**
     * 연결 해제
     */
    disconnect() {
        if (this.ws) {
            this.ws.close(1000, 'User disconnected');
            this.ws = null;
        }
        this.isConnected = false;
        this.isConnecting = false;
        this.sessionId = null;
        this.reconnectAttempts = 0;
        this.updateStatus('disconnected', '연결 해제됨');
    }

    /**
     * 재연결 시도
     */
    attemptReconnect() {
        if (this.reconnectAttempts >= this.maxReconnectAttempts) {
            console.warn('최대 재연결 시도 횟수 초과');
            this.updateStatus('error', '재연결 실패');
            return;
        }

        this.reconnectAttempts++;
        this.updateStatus('connecting', `재연결 중... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);

        setTimeout(() => {
            if (this.sessionId && !this.isConnected && !this.isConnecting) {
                console.log(`재연결 시도 ${this.reconnectAttempts}/${this.maxReconnectAttempts}`);
                this.connect(this.sessionId);
            }
        }, this.reconnectInterval);
    }

    /**
     * 연결 상태 업데이트
     * @param {string} status - 상태 (connected, connecting, disconnected, error)
     * @param {string} text - 상태 텍스트
     */
    updateStatus(status, text) {
        const indicator = document.getElementById('status-indicator');
        const statusText = document.getElementById('status-text');

        if (indicator && statusText) {
            // 기존 상태 클래스 제거
            indicator.className = indicator.className.replace(/status-\w+/g, '');
            // 새 상태 클래스 추가
            indicator.classList.add('status-indicator', `status-${status}`);
            statusText.textContent = text;
        }

        // 버튼 상태 업데이트
        this.updateButtonStates(status);
    }

    /**
     * 버튼 상태 업데이트
     * @param {string} status
     */
    updateButtonStates(status) {
        const connectBtn = document.getElementById('btn-connect');
        const disconnectBtn = document.getElementById('btn-disconnect');

        if (connectBtn && disconnectBtn) {
            switch (status) {
                case 'connected':
                    connectBtn.disabled = true;
                    disconnectBtn.disabled = false;
                    break;
                case 'connecting':
                    connectBtn.disabled = true;
                    disconnectBtn.disabled = true;
                    break;
                case 'disconnected':
                case 'error':
                default:
                    connectBtn.disabled = false;
                    disconnectBtn.disabled = true;
                    break;
            }
        }
    }

    /**
     * 제어 문자 정규화 메서드
     * @param {string} data
     * @returns {string}
     */
    normalizeControlCharacters(data) {
        // 일반적인 제어 문자 정규화
        return data
            .replace(/\r\n/g, '\r\n')      // CRLF 정규화
            .replace(/\r(?!\n)/g, '\r\n'); // 단독 CR을 CRLF로 변환
    }

    /**
     * 연결 상태 확인
     * @returns {boolean}
     */
    isWebSocketConnected() {
        return this.isConnected && this.ws && this.ws.readyState === WebSocket.OPEN;
    }
}

// 전역 인스턴스 생성
window.sshClient = new SSHWebSocketClient();
