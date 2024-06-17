<template>
  <nav class="bg-neutral z-50 px-4 py-2 flex fixed w-full justify-between items-center h-1/8 drop-shadow-lg">

    <div class="flex"> 
      <button @click="toggleSidebar" class="lg:hidden btn btn-ghost" v-if="user">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="3" y1="12" x2="21" y2="12"></line>
          <line x1="3" y1="6" x2="21" y2="6"></line>
          <line x1="3" y1="18" x2="21" y2="18"></line>
        </svg>
      </button>
      <div class="dropdown">
        <div tabindex="0" role="button" class="btn btn-ghost h-3">
          <div class="text-xl font-bold font-mono tracking-tighter">
            Mouse Admin
          </div>
        </div>
      </div>
    </div>

    <!--右侧控件-->
    <div class="hidden sm:flex space-x-4">
      <label class="flex cursor-pointer gap-2">
        <!--主题控制器-->
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none"
          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="12" r="5" />
          <path
            d="M12 1v2M12 21v2M4.2 4.2l1.4 1.4M18.4 18.4l1.4 1.4M1 12h2M21 12h2M4.2 19.8l1.4-1.4M18.4 5.6l1.4-1.4" />
        </svg>

        <input type="checkbox" class="toggle theme-controller" @change="toggleTheme" v-model="isDarkMode" />

        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none"
          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
        </svg>
      </label>
    </div>
  </nav>

  <transition name="slide">
    <div v-if="isSidebarOpen" class="fixed inset-y-0 right-0 bg-neutral text-white w-full sm:w-1/6 p-4 z-10 pt-16">
      <ul class="menu w-full sm:w-56 rounded-box">
        <li @click="gotoUserDetails(user.id)" v-if="user">
          <div>
            <div class="flex gap-5 items-center my-2">
              <div class="avatar online">
                <div class="w-11 rounded-full">
                  <img :src="user.avatar" alt="User Avatar" />
                </div>
              </div>
              <div class="flex flex-col">
                <h1 class="text-xl font-black font-mono">{{ user.username }}</h1>
              </div>
            </div>
          </div>
        </li>
        <li>
          <a>
            <el-icon>
              <House />
            </el-icon>
            <router-link to="/">主页</router-link>
          </a>
        </li>
        <li>
          <a>
            <el-icon>
              <User />
            </el-icon>
            <router-link to="/customers">客户</router-link>
          </a>
        </li>
        <li>
          <a>
            <el-icon>
              <Avatar />
            </el-icon>
            <router-link to="/employees">员工</router-link>
          </a>
        </li>
        <li>
          <a>
            <el-icon>
              <Tickets />
            </el-icon>
            <router-link to="/orders">订单</router-link>
          </a>
        </li>
        <li>
          <a>
            <el-icon>
              <ShoppingTrolley />
            </el-icon>
            <router-link to="/products">产品</router-link>
          </a>
        </li>
      </ul>

      <div class="absolute bottom-4 inset-x-0 mb-2">
        <div class="flex justify-center">
          <ul class="menu menu-horizontal">
            <li>
              <a @click="logout">
                <el-icon>
                  <House />
                </el-icon>
                登出
              </a>
            </li>
            <li>
              <a>
                <el-icon>
                  <FolderAdd />
                </el-icon>
                <backup />
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import { House, User, Avatar, Tickets, ShoppingTrolley, FolderAdd } from '@element-plus/icons-vue';
import backup from './backup/BackupButton.vue';


const store = useStore();
const router = useRouter();

const isSidebarOpen = ref(false); // 控制侧边栏的显示隐藏
const isDarkMode = ref(false);

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
};

const toggleTheme = () => {
  if (isDarkMode.value) {
    document.documentElement.setAttribute('data-theme', 'aqua');
    localStorage.setItem('theme', 'aqua');
  } else {
    document.documentElement.setAttribute('data-theme', 'pastel');
    localStorage.setItem('theme', 'pastel');
  }
};

onMounted(() => {
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme) {
    isDarkMode.value = savedTheme === 'aqua';
    document.documentElement.setAttribute('data-theme', savedTheme);
  } else {
    isDarkMode.value = false;
    document.documentElement.setAttribute('data-theme', 'pastel');
  }
});

const logout = async () => {
  await store.dispatch('logout');
  router.push('/login');
};

const user = computed(() => store.state.users.user);

const gotoUserDetails = (id) => {
  router.push(`/user/${id}`);
};
</script>


<style scoped>
.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
}
</style>