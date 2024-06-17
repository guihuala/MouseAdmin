<template>
  <div class="fixed inset-y-0 right-0 bg-neutral text-white w-1/6 p-4 z-10 pt-16">
    <ul class="menu w-56 rounded-box">
      <li @click="gotoUserDetails(user.id)" v-if="user">
        <el-popover :width="250" :show-arrow="false"
          popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;">
          <template #reference>
            <div>
              <div class="flex gap-5 items-center my-2">
                <div class="avatar online">
                  <div class="w-11 rounded-full">
                    <img :src="user.avatar" alt="User Avatar" />
                  </div>
                </div>
                <div class="flex flex-col">
                  <h1 class="text-xl font-black font-mono">{{ user.username }}</h1>
                  <div class="stat-desc text-secondary">online</div>
                </div>
              </div>
            </div>
          </template>
          <template #default>
            <div class="demo-rich-conent" style="display: flex; gap: 10px; flex-direction: column">
              <el-avatar :size="60" :src="user.avatar" alt="User Avatar" style="margin-bottom: 4px" />
              <div>
                <p class="demo-rich-content__name" style="margin: 0; font-weight: 800;font-size: large;">
                  {{ user.username }}
                </p>
              </div>
              <p class="demo-rich-content__desc" style="margin: 0">
                {{ user.username }},欢迎回到Mouse Admin!
              </p>
            </div>
          </template>
        </el-popover>
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

    <div class="absolute bottom-0 inset-x-0 mb-2">
      <div class="flex justify-center">
        <ul class="menu lg:menu-horizontal rounded-box">
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
</template>

<script setup>
import backup from './backup/BackupButton.vue';
import { computed } from 'vue';
import { House, User, Avatar, Tickets, ShoppingTrolley, FolderAdd } from '@element-plus/icons-vue'
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';

const store = useStore();
const router = useRouter();

const logout = async () => {
  await store.dispatch('logout');
  router.push('/login');
}

const user = computed(() => store.state.users.user);

const gotoUserDetails = (id) => {
  router.push(`/user/${id}`);
}
</script>

<style>
:root {
  .el-popper {
    --el-popover-border-radius: 30px;
    --el-popover-padding: 20px;
  }
}
</style>