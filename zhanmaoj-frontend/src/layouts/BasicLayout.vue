<template>
  <div id="basicLayout">
    <a-layout style="height: 400px;">
      <a-layout-header class="header">
        <GlobalHeader/>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">战码-Java练题网站</a-layout-footer>
    </a-layout>
  </div>
</template>

<style>
#basicLayout {

}

#basicLayout .header {
  margin-bottom: 16px;
  box-shadow: #eee 1px 1px 5px;
  padding: 20px;
}

#basicLayout .content {
  background-color: var(--color-fill-2);
  padding: 16px 20px 0;
  display: flex;
}

#basicLayout .footer {
  background: #efefef;
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
import GlobalHeader from "@/components/GlobalHeader.vue"
</script>