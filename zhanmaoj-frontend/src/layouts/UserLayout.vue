<template>
  <div id="userLayout">
    <a-layout style="height: 400px;">
      <a-layout-header class="header">
        <a-space class="logos">
          <img
              alt="logo"
              src="https://i.postimg.cc/ZqVbxPgh/ojlogo.jpg"
              style="width: 50px"
          />
          <a-typography-title
              :style="{ margin: 0, fontSize: '18px', color: 'black' }"
              :heading="5"
          >
            战码-Java练题网站
          </a-typography-title>
        </a-space>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">战码-Java练题网站</a-layout-footer>
    </a-layout>
  </div>
</template>

<style>
#userLayout {
  text-align: center;
}

#userLayout .logos {
  position: fixed;
  top: 24px;
  left: 22px;
  z-index: 1;
  display: inline-flex;
  align-items: center;
}

#userLayout .header {
  display: flex;
  justify-content: space-between;
  height: 25%;
  border-bottom: 1px solid var(--color-border);
}

#userLayout .content {
  margin-top: 40px;
}

#userLayout .footer {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  text-align: center;
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100%;
}

</style>
<script setup lang="ts">
</script>