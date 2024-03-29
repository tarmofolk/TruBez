<#-- кусок - навигационная панель  -->


<#-- подключаем секьюрити (проверку админ или нет) и логин -->
<#include "security.ftl">

<#import "login.ftl" as l>


<nav class="navbar navbar-expand-lg navbar-light bg-light">

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Home </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/main">List of employees </a>
            </li>

            <#if isAdmin>
            <li class="nav-item">
                <a class="nav-link" href="/user">User list </a>
            </li>
            </#if>


            <#if user??>
            <li class="nav-item">
                <a class="nav-link" href="/user/profile">Profile </a>
            </li>
            </#if>

        </ul>
                <#-- пишем имя подкючившегося юзера -->
        <div class="navbar-text mr-3">
            ${name}
        </div>
        <div>
            <@l.logout />
        </div>
    </div>
</nav>

