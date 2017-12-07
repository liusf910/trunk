<style type="text/css">
    .table.table-condensed > tbody > tr {
        height: 40px;
    }
</style>
<script type="text/javascript">
    //选择事件
    function S_NodeCheck(e, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(treeId),
                nodes = zTree.getCheckedNodes(true);
        var ids = '';

        for (var i = 0; i < nodes.length; i++) {
            ids += ',' + nodes[i].id;
        }
        if (ids.length > 0) {
            ids = ids.substr(1);
        }

        /* var $from = $('#' + treeId).data('fromObj')

         if ($from && $from.length) $from.val(names)*/
        $('#j_ztree_menus1').val(ids);
    }
    //单击事件
    function S_NodeClick(event, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(treeId);

        zTree.checkNode(treeNode, !treeNode.checked, true, true);

        event.preventDefault();
    }
</script>
<div class="bjui-pageContent">
    <div style="margin:15px auto;">
        <form id="edit_role_form" action="${request.contextPath}/setting/role/edit" class="pageForm" method="post"
              data-toggle="validate"
              data-reload-navtab="true">
            <table class="table table-condensed table-hover">
                <tr>
                    <td align="right">角色名称：</td>
                    <td>
                        <input type="hidden" name="roleId" value="${role.roleId!""}"/>
                        <input type="text" name="roleName" value="${role.roleName!""}" data-rule="required" size="20"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">选择权限：</td>
                    <td>
                        <input type="hidden" name="menus" id="j_ztree_menus1" data-toggle="selectztree"
                               data-tree="#j_select_tree1" value="${menus!""}">
                        <ul id="j_select_tree1" class="ztree show" data-toggle="ztree" data-expand-all="true"
                            data-check-enable="true" data-chk-style="checkbox" data-chkbox-type='{"Y" : "ps", "N" : "s"}'
                            data-on-check="S_NodeCheck" data-on-click="S_NodeClick">
                        <#if pmsList??>
                            <#list pmsList as pms>
                                <li data-id="${pms.mid}" data-pid="${pms.pid}"
                                    <#if pms.isChecked>data-checked="true"</#if>>${pms.name}</li>
                            </#list>
                        </#if>
                        </ul>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <button type="button" class="btn-close">关闭</button>
        </li>
        <li>
            <button type="submit" class="btn-default">保存</button>
        </li>
    </ul>
</div>