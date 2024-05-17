// ErrorPage.jsx
import React, {useEffect} from 'react';

import { Link } from 'react-router-dom';
import '../scss/ErrorPage.scss';
import Header from "./Header.jsx";

const ErrorPage = () => {

    useEffect(() => {
        document.title = "에러 페이지"
    }, []);
    return (
        <div className="error-page">
            <Header />
            <div className="error-content">
                <h1>404</h1>
                <h2>페이지를 찾을 수 없습니다</h2>
                <p>요청하신 페이지가 존재하지 않거나 이동되었을 수 있습니다.</p>
                <Link to="/" className="go-home-link">
                    홈으로 돌아가기
                </Link>
            </div>
        </div>
    );
};

export default ErrorPage;