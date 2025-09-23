/**
 * xterm.js 터미널 핸들러
 * 터미널 초기화 및 이벤트 처리
 */
class TerminalHandler {
    constructor() {
        this.terminal = null;
        this.fitAddon = null;
        this.webLinksAddon = null;
        this.isInitialized = false;
    }

    /**
     * 터미널 초기화
     */
    initialize() {
        if (this.isInitialized) {
            console.warn('터미널이 이미 초기화되었습니다.');
            return;
        }

        try {
            // xterm.js 터미널 생성
            this.terminal = new Terminal({
                cols: 80,
                rows: 24,
                cursorBlink: true,
                cursorStyle: 'block',
                bellStyle: 'sound',
                fontFamily: 'Consolas, "Liberation Mono", Monaco, "Courier New", monospace',
                fontSize: 14,
                lineHeight: 1.2,
                theme: {
                    background: '#1e1e1e',
                    foreground: '#ffffff',
                    cursor: '#ffffff',
                    cursorAccent: '#1e1e1e',
                    selection: '#rgba(255, 255, 255, 0.3)',
                    black: '#000000',
                    red: '#cd3131',
                    green: '#0dbc79',
                    yellow: '#e5e510',
                    blue: '#2472c8',
                    magenta: '#bc3fbc',
                    cyan: '#11a8cd',
                    white: '#e5e5e5',
                    brightBlack: '#666666',
                    brightRed: '#f14c4c',
                    brightGreen: '#23d18b',
                    brightYellow: '#f5f543',
                    brightBlue: '#3b8eea',
                    brightMagenta: '#d670d6',
                    brightCyan: '#29b8db',
                    brightWhite: '#ffffff'
                }
            });

            // 애드온 초기화
            this.fitAddon = new FitAddon.FitAddon();
            this.webLinksAddon = new WebLinksAddon.WebLinksAddon();

            // 애드온 로드
            this.terminal.loadAddon(this.fitAddon);
            this.terminal.loadAddon(this.webLinksAddon);

            // 터미널을 DOM에 마운트
            const terminalElement = document.getElementById('terminal');
            if (!terminalElement) {
                throw new Error('터미널 엘리먼트를 찾을 수 없습니다.');
            }

            this.terminal.open(terminalElement);
            this.fitAddon.fit();

            // 이벤트 핸들러 설정
            this.setupEventHandlers();

            // 전역 변수로 설정
            window.terminal = this.terminal;

            this.isInitialized = true;

            // 초기 메시지 표시
            this.showWelcomeMessage();

            console.log('터미널이 성공적으로 초기화되었습니다.');

        } catch (error) {
            console.error('터미널 초기화 실패:', error);
            throw error;
        }
    }

    /**
     * 이벤트 핸들러 설정
     */
    setupEventHandlers() {
        // 터미널 입력 이벤트
        this.terminal.onData(data => {
            if (window.sshClient && window.sshClient.isWebSocketConnected()) {
                window.sshClient.send(data);
            } else {
                // WebSocket이 연결되지 않은 경우 로컬 에코
                if (data === '\r') {
                    this.terminal.write('\r\n');
                } else if (data === '\u007f') { // Backspace
                    this.terminal.write('\b \b');
                } else {
                    this.terminal.write(data);
                }
            }
        });

        // 터미널 크기 변경 이벤트
        this.terminal.onResize(size => {
            console.log(`터미널 크기 변경: ${size.cols}x${size.rows}`);
            // SSH 서버에 터미널 크기 변경 알림 (향후 구현)
        });

        // 창 크기 변경 시 터미널 크기 자동 조정
        window.addEventListener('resize', () => {
            if (this.fitAddon) {
                setTimeout(() => {
                    this.fitAddon.fit();
                }, 100);
            }
        });

        // 터미널 포커스 이벤트 (안전한 방식으로 처리)
        try {
            if (typeof this.terminal.onFocus === 'function') {
                this.terminal.onFocus(() => {
                    console.log('터미널이 포커스를 받았습니다.');
                });
            }

            if (typeof this.terminal.onBlur === 'function') {
                this.terminal.onBlur(() => {
                    console.log('터미널이 포커스를 잃었습니다.');
                });
            }
        } catch (error) {
            console.warn('터미널 포커스 이벤트 설정 실패:', error);
            // 대안으로 DOM 이벤트 사용
            const terminalElement = document.getElementById('terminal');
            if (terminalElement) {
                terminalElement.addEventListener('focus', () => {
                    console.log('터미널이 포커스를 받았습니다. (DOM 이벤트)');
                }, true);

                terminalElement.addEventListener('blur', () => {
                    console.log('터미널이 포커스를 잃었습니다. (DOM 이벤트)');
                }, true);
            }
        }
    }

    /**
     * 환영 메시지 표시
     */
    showWelcomeMessage() {
        const welcomeMessage = [
            '\x1b[36m╔══════════════════════════════════════════╗\x1b[0m',
            '\x1b[36m║\x1b[0m           \x1b[1mPlobin SSH Terminal\x1b[0m           \x1b[36m║\x1b[0m',
            '\x1b[36m╚══════════════════════════════════════════╝\x1b[0m',
            '',
            '\x1b[33m⚡ SSH 터미널에 오신 것을 환영합니다!\x1b[0m',
            '',
            '\x1b[32m사용법:\x1b[0m',
            '  • 상단의 \x1b[1m"연결"\x1b[0m 버튼을 클릭하여 SSH 서버에 연결하세요.',
            '  • 연결 후 터미널에서 명령어를 입력할 수 있습니다.',
            '  • \x1b[1m"지우기"\x1b[0m 버튼으로 터미널을 초기화할 수 있습니다.',
            '',
            '\x1b[90m팁: 터미널 크기는 창 크기에 맞춰 자동으로 조정됩니다.\x1b[0m',
            '',
            '─'.repeat(50),
            ''
        ].join('\r\n');

        this.terminal.write(welcomeMessage);
    }

    /**
     * 터미널 지우기
     */
    clear() {
        if (this.terminal) {
            this.terminal.clear();
            this.showWelcomeMessage();
        }
    }

    /**
     * 터미널 크기 자동 조정
     */
    fit() {
        if (this.fitAddon) {
            this.fitAddon.fit();
        }
    }

    /**
     * 터미널에 메시지 출력
     * @param {string} message
     * @param {string} type - 메시지 타입 (info, success, warning, error)
     */
    writeMessage(message, type = 'info') {
        if (!this.terminal) return;

        const colors = {
            info: '\x1b[36m',     // 청록색
            success: '\x1b[32m',  // 녹색
            warning: '\x1b[33m',  // 노란색
            error: '\x1b[31m'     // 빨간색
        };

        const reset = '\x1b[0m';
        const color = colors[type] || colors.info;

        this.terminal.write(`\r\n${color}${message}${reset}\r\n`);
    }

    /**
     * 연결 정보 표시
     * @param {string} host
     * @param {number} port
     * @param {string} username
     */
    showConnectionInfo(host, port, username) {
        const connectionInfo = document.getElementById('connection-info');
        if (connectionInfo) {
            connectionInfo.textContent = `${username}@${host}:${port}`;
        }

        this.writeMessage(`SSH 연결 시도: ${username}@${host}:${port}`, 'info');
    }

    /**
     * 연결 해제 시 정보 지우기
     */
    clearConnectionInfo() {
        const connectionInfo = document.getElementById('connection-info');
        if (connectionInfo) {
            connectionInfo.textContent = '';
        }
    }

    /**
     * 터미널 소멸
     */
    destroy() {
        if (this.terminal) {
            this.terminal.dispose();
            this.terminal = null;
        }
        this.fitAddon = null;
        this.webLinksAddon = null;
        this.isInitialized = false;
        window.terminal = null;
    }
}

// 전역 인스턴스 생성
window.terminalHandler = new TerminalHandler();