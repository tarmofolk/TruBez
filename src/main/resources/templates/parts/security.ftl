<#-- кусок секьюрити  (скрывает от не админа кнопку эдит юзеров) -->





<#-- определяем существует ли у нас в контексте необходимый объект -->

<#assign
    known = Session.SPRING_SECURITY_CONTEXT??
>

<#-- если объект определен в контексте, то мы можем работать с сессией пользователя -->
<#if known>

        <#-- пользователь, которого мы описывали в базе данных -->
        <#-- достаем метод геттер для юзернэйма (получаем пользователя) -->
        <#-- проверяем является ли админом -->
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        isAdmin = user.isAdmin()
    >

<#-- иначе fullback -->
<#else>
    <#assign
        name = "unknown"
        isAdmin = false
    >

</#if>
