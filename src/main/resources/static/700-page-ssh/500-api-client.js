/**
 * SSH API 클라이언트
 * 백엔드 SSH API와 통신하는 클래스
 * Updated: 2025-09-23 - JSON 파싱 오류 수정
 */
class SSHApiClient {
    constructor() {
        // 현재 페이지와 같은 호스트와 포트를 사용
        const protocol = window.location.protocol;
        const host = window.location.host;
        this.baseUrl = `${protocol}//${host}/api/ssh`;
    }

    /**
     * SSH 서버에 연결
     * @param {string} host
     * @param {number} port
     * @param {string} username
     * @param {string} password
     * @returns {Promise<string>} sessionId
     */
    async connect(host, port, username, password) {
        try {
            const response = await fetch(`${this.baseUrl}/connect`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    host: host,
                    port: parseInt(port),
                    username: username,
                    password: password
                })
            });

            const result = await response.json();

            if (result.success) {
                console.log('SSH 연결 성공:', result.data);
                return {
                    sessionId: result.data.sessionId,
                    connectionId: result.data.connectionId,
                    websocketUrl: result.data.websocketUrl
                };
            } else {
                throw new Error(result.message);
            }
        } catch (error) {
            console.error('SSH 연결 실패:', error);
            throw new Error(`SSH 연결 실패: ${error.message}`);
        }
    }

    /**
     * SSH 연결 해제
     * @param {string} sessionId
     * @returns {Promise<object>}
     */
    async disconnect(sessionId) {
        try {
            const response = await fetch(`${this.baseUrl}/disconnect/${sessionId}`, {
                method: 'DELETE'
            });

            // 응답이 비어있는지 확인
            const text = await response.text();
            console.log('Disconnect response status:', response.status);
            console.log('Disconnect response text:', text);

            if (!text || text.trim() === '') {
                // 빈 응답인 경우 성공으로 처리
                if (response.ok) {
                    console.log('SSH 연결 해제 성공 (빈 응답)');
                    return { success: true, message: 'SSH 연결이 해제되었습니다' };
                } else {
                    throw new Error(`HTTP ${response.status}: ${response.statusText || 'Unknown error'}`);
                }
            }

            // JSON 파싱 시도
            let result;
            try {
                result = JSON.parse(text);
            } catch (parseError) {
                console.error('JSON 파싱 실패:', parseError);
                console.error('응답 텍스트:', text);
                throw new Error(`서버 응답 파싱 실패: ${text.substring(0, 100)}`);
            }

            if (result.success) {
                console.log('SSH 연결 해제 성공');
                return result;
            } else {
                throw new Error(result.message || '알 수 없는 오류');
            }
        } catch (error) {
            console.error('SSH 연결 해제 실패:', error);

            // 404 오류의 경우 이미 연결이 해제된 것으로 간주
            if (error.message && error.message.includes('404')) {
                console.log('세션이 이미 해제되었거나 존재하지 않습니다.');
                return { success: true, message: '연결이 이미 해제되었습니다' };
            }

            throw new Error(`SSH 연결 해제 실패: ${error.message || 'Unknown error'}`);
        }
    }

    /**
     * SSH 연결 상태 조회
     * @returns {Promise<object>}
     */
    async getStatus() {
        try {
            const response = await fetch(`${this.baseUrl}/status`);
            const result = await response.json();

            if (result.success) {
                return result.data;
            } else {
                throw new Error(result.message);
            }
        } catch (error) {
            console.error('SSH 상태 조회 실패:', error);
            throw new Error(`SSH 상태 조회 실패: ${error.message}`);
        }
    }
}

// 전역 인스턴스 생성
window.sshApi = new SSHApiClient();
