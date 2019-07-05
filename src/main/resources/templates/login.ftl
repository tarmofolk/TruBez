<#-- форма логина -->


<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>


<@c.page>

<#-- Если присутствует сессия (пользователь авторизован) и нет поля ААА то выводим алерт -->
<#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
    <div class="alert alert-danger" role="alert">
        ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
    </div>
</#if>


<#-- Если присутствует сообщение (об ошибке или успехе), то смотрим тип сообщение и выводим это самое сообщение, выделенное правильным цветом -->
<#if message??>
<div class="alert alert-${messageType}" role="alert">
${message}
</div>
</#if>



<#-- вставляем из кусков кусок логина, указываем, что для мэпинга логин и то, что это НЕ форма регитрации-->
<@l.login "/login" false/>

</@c.page>
