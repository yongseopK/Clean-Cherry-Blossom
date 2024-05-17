import React, {useEffect, useState} from 'react';
import {FORGET_PASSWORD} from "../../../config/host-config.jsx";

const ForgetPassword = ({isOpen, onClose}) => {

    const [email, setEmail] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    };

    useEffect(() => {
        document.title = "비밀번호 찾기";
    }, []);

    const handleSubmit = async () => {
        setIsLoading(true); // 요청 시작 전에 로딩 상태로 설정

        try {
            const response = await fetch(`${FORGET_PASSWORD}?email=${email}`, {
                method: "POST",
            });

            if (response.status === 200) {
                alert("임시 비밀번호가 발급되었습니다. 빠른 시일 내에 비밀번호를 변경해 주세요.");
                onClose();
            }
        } catch (error) {
            console.error("Error:", error);
            // 에러 처리 로직 추가
        } finally {
            setIsLoading(false); // 요청 완료 후 로딩 상태 해제
        }
    };

    const LoadingSpinner = () => (
        <div className="loading-spinner">
            <div className="spinner"></div>
        </div>
    );

    if (!isOpen) return null;

    return (
        <>
            {isLoading && <LoadingSpinner />}
            <div className="popup">
                <div className="popup-content">
                    <h2>비밀번호 찾기</h2>
                    <input
                        type="email"
                        value={email}
                        placeholder="이메일 주소를 입력하세요"
                        onChange={handleEmailChange}
                    />
                    <button onClick={handleSubmit} disabled={isLoading}>
                        비밀번호 재설정
                    </button>
                    <button onClick={onClose} disabled={isLoading}>
                        닫기
                    </button>
                </div>
            </div>
        </>
    );
};

export default ForgetPassword;