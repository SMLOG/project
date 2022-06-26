module.exports = {
  publicPath: "",
  productionSourceMap: false,
  filenameHashing: false,
  css: {
    // 是否使用css分离插件 ExtractTextPlugin
    extract: true,
    // 开启 CSS source maps?
    sourceMap: false,
    // css预设器配置项
    loaderOptions: {},
    // 启用 CSS modules for all css / pre-processor files.
    modules: false,
  },
  devServer: {
    disableHostCheck: true,
    port: 8082,
    overlay: { infos: true, warnings: true, errors: true },
    proxy: {
      "/api": {
        target: "http://localhost:8081",
        changeOrigin: true,
	ws:true,
	pathRewrite:{
		"^/api":"/api"
	}
      },
    },
  },
};
