/**
 * SSH 터미널 메인 컨트롤러
 * 전체 애플리케이션 로직 관리
 */
class SSHTerminalController {
    constructor() {
        this.currentSessionId = null;
        this.currentConnection = null;
        this.isConnected = false;
        this.connectionModal = null;
    }

    /**
     * 애플리케이션 초기화
     */
    initialize() {
        try {
            // 터미널 초기화
            window.terminalHandler.initialize();

            // 모달 초기화
            this.connectionModal = new bootstrap.Modal(document.getElementById('sshConnectionModal'));

            // 이벤트 리스너 설정
            this.setupEventListeners();

            // 저장된 연결 정보 로드
            this.loadSavedConnections();

            console.log('SSH 터미널 컨트롤러가 초기화되었습니다.');

        } catch (error) {
            console.error('애플리케이션 초기화 실패:', error);
            this.showError('애플리케이션 초기화에 실패했습니다: ' + error.message);
        }
    }

    /**
     * 이벤트 리스너 설정
     */
    setupEventListeners() {
        // 연결 버튼
        document.getElementById('btn-connect').addEventListener('click', () => {
            this.showConnectionModal();
        });

        // 연결 해제 버튼
        document.getElementById('btn-disconnect').addEventListener('click', () => {
            this.disconnect();
        });

        // 터미널 지우기 버튼
        document.getElementById('btn-clear').addEventListener('click', () => {
            window.terminalHandler.clear();
        });

        // 전체화면 버튼
        document.getElementById('btn-fullscreen').addEventListener('click', () => {
            this.toggleFullscreen();
        });

        // SSH 연결 모달의 연결 버튼
        document.getElementById('btn-connect-ssh').addEventListener('click', () => {
            this.connectToSSH();
        });

        // SSH 연결 폼 엔터키 처리
        document.getElementById('ssh-connection-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.connectToSSH();
        });

        // 모달 닫힐 때 스피너 숨기기
        document.getElementById('sshConnectionModal').addEventListener('hidden.bs.modal', () => {
            this.hideConnectSpinner();
        });

        // 페이지 종료 시 연결 해제
        window.addEventListener('beforeunload', () => {
            if (this.isConnected) {
                this.disconnect();
            }
        });
    }

    /**
     * 연결 모달 표시
     */
    showConnectionModal() {
        this.connectionModal.show();
        // 모달이 표시된 후 첫 번째 입력 필드에 포커스
        setTimeout(() => {
            document.getElementById('ssh-host').focus();
        }, 300);
    }

    /**
     * SSH 서버에 연결
     */
    async connectToSSH() {
        const host = document.getElementById('ssh-host').value.trim();
        const port = parseInt(document.getElementById('ssh-port').value);
        const username = document.getElementById('ssh-username').value.trim();
        const password = document.getElementById('ssh-password').value;
        const saveConnection = document.getElementById('save-connection').checked;

        // 입력 값 검증
        if (!host || !username || !password) {
            this.showError('모든 필드를 입력해주세요.');
            return;
        }

        if (port < 1 || port > 65535) {
            this.showError('포트는 1-65535 범위여야 합니다.');
            return;
        }

        this.showConnectSpinner();

        try {
            // 터미널에 연결 시도 메시지 표시
            window.terminalHandler.showConnectionInfo(host, port, username);

            // SSH API 호출
            const result = await window.sshApi.connect(host, port, username, password);

            this.currentSessionId = result.sessionId;
            this.currentConnection = { host, port, username };

            // WebSocket 연결
            window.sshClient.connect(result.sessionId);

            this.isConnected = true;

            // 연결 정보 저장 (옵션)
            if (saveConnection) {
                this.saveConnectionInfo(host, port, username);
            }

            // 모달 닫기
            this.connectionModal.hide();
            this.hideConnectSpinner();

            console.log('SSH 연결 성공:', result);

        } catch (error) {
            console.error('SSH 연결 실패:', error);
            this.showError(error.message);
            this.hideConnectSpinner();
            window.terminalHandler.writeMessage(`연결 실패: ${error.message}`, 'error');
        }
    }

    /**
     * SSH 연결 해제
     */
    async disconnect() {
        if (!this.isConnected || !this.currentSessionId) {
            console.warn('연결된 세션이 없습니다.');
            return;
        }

        try {
            // WebSocket 연결 해제
            window.sshClient.disconnect();

            // SSH API 호출하여 서버 측 연결 해제
            await window.sshApi.disconnect(this.currentSessionId);

            this.currentSessionId = null;
            this.currentConnection = null;
            this.isConnected = false;

            // 연결 정보 지우기
            window.terminalHandler.clearConnectionInfo();
            window.terminalHandler.writeMessage('SSH 연결이 해제되었습니다.', 'info');

            console.log('SSH 연결 해제 완료');

        } catch (error) {
            console.error('SSH 연결 해제 실패:', error);
            this.showError(`연결 해제 실패: ${error.message}`);
        }
    }

    /**
     * 전체화면 토글
     */
    toggleFullscreen() {
        const terminalWrapper = document.querySelector('.terminal-wrapper');

        if (!document.fullscreenElement) {
            // 전체화면 진입
            terminalWrapper.requestFullscreen().then(() => {
                setTimeout(() => {
                    window.terminalHandler.fit();
                }, 100);
            }).catch(err => {
                console.error('전체화면 진입 실패:', err);
            });
        } else {
            // 전체화면 해제
            document.exitFullscreen().then(() => {
                setTimeout(() => {
                    window.terminalHandler.fit();
                }, 100);
            }).catch(err => {
                console.error('전체화면 해제 실패:', err);
            });
        }
    }

    /**
     * 연결 스피너 표시
     */
    showConnectSpinner() {
        const spinner = document.getElementById('connect-spinner');
        const button = document.getElementById('btn-connect-ssh');

        if (spinner) {
            spinner.classList.remove('d-none');
        }
        if (button) {
            button.disabled = true;
            button.textContent = ' 연결 중...';
        }
    }

    /**
     * 연결 스피너 숨기기
     */
    hideConnectSpinner() {
        const spinner = document.getElementById('connect-spinner');
        const button = document.getElementById('btn-connect-ssh');

        if (spinner) {
            spinner.classList.add('d-none');
        }
        if (button) {
            button.disabled = false;
            button.textContent = '연결';
        }
    }

    /**
     * 오류 메시지 표시
     * @param {string} message
     */
    showError(message) {
        // 간단한 알림으로 표시 (추후 토스트 메시지로 개선 가능)
        alert(`오류: ${message}`);
    }

    /**
     * 연결 정보 저장
     * @param {string} host
     * @param {number} port
     * @param {string} username
     */
    saveConnectionInfo(host, port, username) {
        try {
            const connectionInfo = { host, port, username };
            localStorage.setItem('ssh_last_connection', JSON.stringify(connectionInfo));
            console.log('연결 정보가 저장되었습니다.');
        } catch (error) {
            console.error('연결 정보 저장 실패:', error);
        }
    }

    /**
     * 저장된 연결 정보 로드
     */
    loadSavedConnections() {
        try {
            const saved = localStorage.getItem('ssh_last_connection');
            if (saved) {
                const connectionInfo = JSON.parse(saved);

                // 폼에 저장된 값 설정
                document.getElementById('ssh-host').value = connectionInfo.host || '';
                document.getElementById('ssh-port').value = connectionInfo.port || 22;
                document.getElementById('ssh-username').value = connectionInfo.username || '';

                console.log('저장된 연결 정보를 로드했습니다.');
            }
        } catch (error) {
            console.error('저장된 연결 정보 로드 실패:', error);
        }
    }

    /**
     * 연결 상태 확인
     * @returns {boolean}
     */
    isSSHConnected() {
        return this.isConnected && window.sshClient.isWebSocketConnected();
    }
}

// DOM 로드 완료 시 애플리케이션 초기화
document.addEventListener('DOMContentLoaded', function() {
    window.sshTerminalController = new SSHTerminalController();
    window.sshTerminalController.initialize();
});