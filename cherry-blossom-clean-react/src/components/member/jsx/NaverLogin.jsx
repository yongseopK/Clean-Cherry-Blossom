import React, {useEffect} from 'react';
import {useLocation, useNavigate} from "react-router-dom";
import axios from "axios";
import {ROLE, TOKEN, USERNAME} from "../../../util/login-util.jsx";

const NaverLogin = () => {

    const location = useLocation();
    const redirection = useNavigate();

    const getUserInfo = async () => {
        if(!location.hash) return;

        const token = location.hash.split('=')[1].split('&')[0];

        const response = await axios.post("http://localhost:8888/api/members/naver", {
            token: token,
        }, {withCredential: true})


        if(response.status ===200) {
            const {token, userName, role} = await response.data;
            localStorage.setItem(TOKEN, token);
            localStorage.setItem(USERNAME, userName);
            localStorage.setItem(ROLE, role);
            window.close();
            window.opener.location.replace('/');
        }
    }

    useEffect(() => {
        getUserInfo();
    }, []);

    return (
        <></>
    );
};

export default NaverLogin;