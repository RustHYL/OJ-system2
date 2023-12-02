<template>
  <div class="home">
    <MarkdownEditor :value="value" :handle-change="onChange"/>
<!--    <CodeEditor style="min-height: 400px; height: 60vh"/>-->
  </div>
</template>

<script setup lang="ts">

import {ref} from "vue";
import MarkdownEditor from "@/components/MarkdownEditor.vue";
import CodeEditor from "@/components/CodeEditor.vue";


const value = ref();

const onChange = (v: string) => {
  value.value = v
}

</script>
