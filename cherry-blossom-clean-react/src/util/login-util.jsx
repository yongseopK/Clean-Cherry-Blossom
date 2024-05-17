export const TOKEN = 'ACCESS_TOKEN';
export const USERNAME = 'USER_NAME';
export const ROLE = 'ROLE';

export const getLogin = () => !!localStorage.getItem(TOKEN);

export const getCurrentLoginUser = () => {
    return {
        token: localStorage.getItem(TOKEN),
        username: localStorage.getItem(USERNAME),
        role: localStorage.getItem(ROLE),
    }
}