import axios from '../../api/axios';

const state = {
    user: JSON.parse(localStorage.getItem('user')) || null,
};
const mutations = {
    setUser(state, user) {
        state.user = user;
        localStorage.setItem('user', JSON.stringify(user));
    },
    clearUser(state) {
        state.user = null;
        localStorage.removeItem('user');
    },
};
const actions = {
    async login({ commit }, { username, password }) {
        try {
            const response = await axios.get('/api/users/login', {
                params: {
                    username,
                    password,
                },
            });
            const { user, token } = response.data;
            localStorage.setItem('jwt_token', `Bearer ${token}`);
            commit('setUser', user);
        } catch (error) {
            console.error('登录失败', error);
            throw error;
        }
    },
    logout({ commit }) {
        localStorage.removeItem('jwt_token');
        commit('clearUser');
    },
    async register({ commit }, user) {
        try {
            const formData = new FormData();
            formData.append('username', user.username);
            formData.append('password', user.password);
            formData.append('email', user.email);
            formData.append('role', user.role);

            const response = await axios.post('/api/users/register', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            commit('setUser', response.data);
        } catch (error) {
            console.error('注册失败', error);
            throw error;
        }
    },
    async fetchCurrentUser({ commit }) {
        try {
            const response = await axios.get('/api/users/info');
            commit('setUser', response.data);
        } catch (error) {
            console.error('获取用户失败', error);
            throw error;
        }
    },
    async updateUser({ commit }, { id, formData }) {
        try {
            const response = await axios.put(`/api/users/update/${id}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            commit('setUser', response.data);
        } catch (error) {
            console.error('更新用户信息失败', error);
            throw error;
        }
    },
    async findUsername({ commit }, { username, email }) {
        try {
            await axios.get('/api/users/findUsername', {
                params: {
                    username,
                    email,
                },
            });
        } catch (error) {
            console.error('要找回密码的用户不存在', error);
            throw error;
        }
    },
    async findPassword({ commit }, { username, email, password }) {
        try {
            await axios.put('/api/users/findPassword', null, {
                params: {
                    username,
                    email,
                    password,
                },
            });
        } catch (error) {
            console.error('找回密码失败', error);
            throw error;
        }
    },
    async deleteUser({ commit }, id) {
        try {
            await axios.delete(`/api/users/delete/${id}`);
            commit('clearUser');
        } catch (error) {
            console.error('删除用户失败', error);
            throw error;
        }
    },
};

export default { state, mutations, actions };