<template>
  <div id="userLayout">
    <a-layout style="height: 400px;">
      <a-layout-header class="header">
        <a-space>
          <img src="../assets/oj-logo.jpg" class="logo">
          <div class="title">战码OJ</div>
        </a-space>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">xxx网站</a-layout-footer>
    </a-layout>
  </div>
</template>

<style>
#userLayout {
  text-align: center;
}

#userLayout .title {
  color: #444;
  margin-left: 16px;
}

#userLayout .logo {
  width: 48px;
}

#userLayout .header {
  margin-top: 16px;
  box-shadow: #eee 1px 1px 5px;
  padding: 20px;
}

#userLayout .content {
  margin-bottom: 16px;
}

#userLayout .footer {
  margin-bottom: 16px;
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
<script setup lang="ts">
</script>