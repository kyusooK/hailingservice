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
    
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => {
        options.compilerOptions = {
          ...options.compilerOptions,
          isCustomElement: tag => tag === 'payment-system-app',
        };
        return options;
      });
  },
  configureWebpack: {
    output: {
      libraryTarget: 'window',
      filename: 'payment-system-app.js',
      libraryExport: 'default',
    },
  },
  transpileDependencies: [
    'vuetify'
  ],
  devServer: {
    disableHostCheck: true
  }
}
