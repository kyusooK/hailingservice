module.exports = {
  chainWebpack: config => {
    config.module
      .rule('js')
      .use('babel-loader')
      .loader('babel-loader')
      .tap(options => {
        return {
          ...options,
          presets: ['@babel/preset-env']
        };
      });
  },
  transpileDependencies: [
    'vuetify'
  ],

  devServer: {
    disableHostCheck: true
  }
}
