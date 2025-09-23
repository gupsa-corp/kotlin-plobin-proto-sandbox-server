/**
 * SSH API 클라이언트
 * 백엔드 SSH API와 통신하는 클래스
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

            const result = await response.json();

            if (result.success) {
                console.log('SSH 연결 해제 성공');
                return result;
            } else {
                throw new Error(result.message);
            }
        } catch (error) {
            console.error('SSH 연결 해제 실패:', error);
            throw new Error(`SSH 연결 해제 실패: ${error.message}`);
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
