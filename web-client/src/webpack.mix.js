const mix = require("laravel-mix");

/*
 |--------------------------------------------------------------------------
 | Mix Asset Management
 |--------------------------------------------------------------------------
 |
 | Mix provides a clean, fluent API for defining some Webpack build steps
 | for your Laravel applications. By default, we are compiling the CSS
 | file for the application as well as bundling up all the JS files.
 |
 */

mix.js("src/assets/js/app.js", "src/assets/dist/js")
    .js("src/assets/js/ckeditor-classic.js", "src/assets/dist/js")
    .js("src/assets/js/ckeditor-inline.js", "src/assets/dist/js")
    .js("src/assets/js/ckeditor-balloon.js", "src/assets/dist/js")
    .js("src/assets/js/ckeditor-balloon-block.js", "src/assets/dist/js")
    .js("src/assets/js/ckeditor-document.js", "src/assets/dist/js")
    .css("src/assets/dist/css/_app.css", "src/assets/dist/css/app.css")
    .options({
        processCssUrls: false,
    })
    .copyDirectory("src/assets/json", "src/assets/dist/json")
    .copyDirectory("src/assets/fonts", "src/assets/dist/fonts")
    .copyDirectory("src/assets/images", "src/assets/dist/images");
