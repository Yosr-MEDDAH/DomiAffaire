(()=>{(function(){"use strict";$(".highlight").each(function(){let l=$(this).find("code").html(),e=helper.replaceAll(l,"HTMLOpenTag","<");e=helper.replaceAll(e,"HTMLCloseTag",">");let t=$('<textarea class="absolute w-0 h-0 p-0 -mt-1 -ml-1"></textarea>').val(e);$(this).append(t),$(this).find("code").hasClass("javascript")?e=jsBeautify(e):e=jsBeautify.html(e),e=helper.replaceAll(e,"<","&lt;"),e=helper.replaceAll(e,">","&gt;"),$(this).find("code").html(e)}),hljs.highlightAll()})();})();