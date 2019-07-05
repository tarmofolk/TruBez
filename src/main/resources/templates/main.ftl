<#-- главная -->



<#import "parts/common.ftl" as c>




<@c.page>

<div class="form-row">
    <div class="form-group col-md-6">
        <form method = "get" action = "/main" class="form-inline">
            <input type = "text" name = "filter" class="form-control" value="${filter?ifExists}" placeholder="Search by firm" />
            <button type = "submit" class="btn btn-primary ml-2"> Search </button>
        </form>
    </div>
</div>





<a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
Add new employee
</a>

<#-- схлопываем. если есть сообщение об ошибке - показываем -->
<div class="collapse <#if employee??>show</#if>" id="collapseExample">
    <div class="form-group mt-3">
        <form method = "post" enctype="multipart/form-data">

            <div class = "form-group">
                <#-- если присутствует сообщение об ошибке, то помечаем доп. классом инвалид -->
                <input type = "text" name = "fullName"
                       class="form-control ${(fullNameError??)?string('is-invalid', '')}"
                       value="<#if employee??>${employee.fullName}</#if>"/>
                <#-- если присутствует сообщение об ошибке, то отображаем поле с пометкой что это ошибка -->
                <#if fullNameError??>
                <div class="invalid-feedback">
                    ${fullNameError}
                </div>
                </#if>
            </div>

            <div class = "form-group">
               <input type = "text" name = "firm"
                      class="form-control"
                      value="<#if employee??>${employee.firm}</#if>"/>
                <#-- если присутствует сообщение об ошибке, то отображаем поле с пометкой что это ошибка -->
                <#if firmError??>
                <div class="invalid-feedback">
                    ${firmError}
                </div>
                </#if>
            </div>

            <div class = "form-group">
                <div class="custom-file" >
                    <input type="file" id = "customFile" name="file"/>
                    <label for="customFile" class="custom-file-label"> Choose file </label>
                </div>
            </div>


            <input type = "hidden" name = "_csrf" value = "${ _csrf.token }" />

            <div class = "form-group">
                 <button type = "submit" class="btn btn-primary"> Добавить </button>
            </div>
        </form>
    </div>
</div>





<div class="card-columns">

    <#list employees as employee>     <!-- отображает лист из контроллера  по мэпингу main-->

    <div class="card my-3" >



        <#if employee.filename??>
            <img src="/img/${employee.filename}" class="card-img-top" />
        </#if>

        <div class="m-2">
            <span> ${employee.fullName} </span>
            <i> <#if employee.firm??>${employee.firm}</#if> </i>
        </div>

        <div class="card-footer text-muted">
            ${employee.authorName}
        </div>

    </div>

    <#else>
    No employees
    </#list>
</div>



</@c.page>




<#-- макросы отображаются @ собакой -->
<#-- предопределенные директивы отображаются # решеткой -->