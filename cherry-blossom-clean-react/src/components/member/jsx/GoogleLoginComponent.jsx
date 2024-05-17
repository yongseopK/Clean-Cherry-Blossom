import {GoogleLogin} from "@react-oauth/google";
import axios from "axios";
import {ROLE, TOKEN, USERNAME} from "../../../util/login-util.jsx";
import {useNavigate} from "react-router-dom";
import {GOOGLE_OAUTH_API_URL} from "../../../config/host-config.jsx";

const GoogleLoginComponent = () => {
    const redirection = useNavigate();

    const onSuccess = async (res) => {
        try {
            const response = await axios.post(GOOGLE_OAUTH_API_URL, {
                credential: res.credential,
            });


            if (response.status === 200) {
                const {token, userName, role} = await response.data;
                localStorage.setItem(TOKEN, token);
                localStorage.setItem(USERNAME, userName);
                localStorage.setItem(ROLE, role);
                redirection('/');
            }
        } catch (err) {
            alert(err.response.data);
        }

    }

    const onFailure = (res) => {
        console.log("LOGIN FAILED! res : ", res);
    }

    return (
        <GoogleLogin
            onSuccess={onSuccess}
            onError={onFailure}
            type={"icon"}
        />
    );
};

export default GoogleLoginComponent;