<template>
  <div class="top-liked-container">
    <Header></Header>

    <div class="content">
      <h2>点赞排行榜</h2>
      <el-card v-if="loading" class="loading-card">
        <i class="el-icon-loading"></i> 正在加载...
      </el-card>
      <el-empty v-else-if="!blogs.length" description="暂无数据"></el-empty>

      <el-card v-else class="list-card">
        <el-timeline>
          <el-timeline-item
            v-for="(blog, index) in blogs"
            :key="blog.id"
            :timestamp="formatDate(blog.created)"
            placement="top"
          >
            <div class="rank-item">
              <span class="rank-no">NO.{{ index + 1 }}</span>
              <router-link
                :to="{ name: 'BlogDetail', params: { blogId: blog.id } }"
                class="rank-title"
              >
                {{ blog.title }}
              </router-link>
              <span class="rank-like">
                <i class="el-icon-thumb"></i> {{ blog.likeCount }} 赞
              </span>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>
  </div>
</template>

<script>
import Header from '../components/Header'

export default {
  name: 'TopLikedBlogs',
  components: { Header },
  data() {
    return {
      blogs: [],
      loading: false
    }
  },
  created() {
    this.loadTopLiked()
  },
  methods: {
    loadTopLiked() {
      const _this = this
      _this.loading = true
      _this.$axios.get('/blogs/top-liked?limit=10').then(res => {
        _this.blogs = (res.data && res.data.data) || []
        _this.loading = false
      }).catch(err => {
        console.error('加载点赞排行榜失败:', err)
        _this.loading = false
        if (err.response && err.response.status === 401) return
        _this.$message.error('加载点赞排行榜失败')
      })
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const d = new Date(dateStr)
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
    }
  }
}
</script>

<style scoped>
.top-liked-container {
  min-height: 100vh;
  background: #34495e;
}
.content {
  max-width: 960px;
  margin: 20px auto;
  padding: 20px;
}
h2 {
  color: #ecf0f1;
  margin-bottom: 20px;
}
.list-card {
  background: #2c3e50;
  border: 1px solid #34495e;
}
.rank-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.rank-no {
  color: #e67e22;
  font-weight: 600;
  margin-right: 10px;
}
.rank-title {
  flex: 1;
  color: #ecf0f1;
  margin: 0 10px;
  text-decoration: none;
}
.rank-title:hover {
  color: #3498db;
}
.rank-like {
  color: #f1c40f;
}
</style>

