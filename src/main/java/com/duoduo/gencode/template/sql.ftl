<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper     PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"     "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

 <!--表 ${tableName}操作方法-->
<mapper namespace="${beanName}Manager">
	<resultMap type="${beanName}" id="${beanName}Map">
		 <#list columns as item>
		 	<result property="${item.attributeName}" column="${item.columnName}" />
		 </#list>
	</resultMap>
    <!--分页查询 记录-->
	<select id="queryPage" parameterType="${beanName}" resultMap="${beanName}Map">
		select * from ${tableName} t
		where 1=1 
		<#list columns as item>
    		<if test="${item.attributeName} != null and ${item.attributeName} != ''">
    			 and t.${item.columnName} = ${'#'}{${item.attributeName}}
    		</if>
        </#list>
        limit ${'#'}{startRow},${'#'}{limit}
	</select>
	<!--分页查询  计数-->
	<select id="queryPageCount" parameterType="${beanName}" resultType="int">
		select count(*) from ${tableName} t
		where 1=1 
		<#list columns as item>
    		<if test="${item.attributeName} != null and ${item.attributeName} != ''">
    			 and t.${item.columnName} = ${'#'}{${item.attributeName}}
    		</if>
        </#list>
	</select>
	<!--查询方法-->
	<select id="select" parameterType="${beanName}" resultMap="${beanName}Map">
		select * from ${tableName} t
		where 1=1 
		<#list columns as item>
    		<if test="${item.attributeName} != null and ${item.attributeName} != ''">
    			 and t.${item.columnName} = ${'#'}{${item.attributeName}}
    		</if>
        </#list>
	</select>
	<!--添加方法-->
	<insert id="insert" parameterType="${beanName}">
		insert into ${tableName}(
			<#list columns as item>
				${item.columnName},
			</#list>
		) values(<#list columns as item>
				${'#'}{${item.attributeName}},
			</#list>)
	</insert>
	<!--更新方法-->
	<update id="update" parameterType="${beanName}">
		update ${tableName} set 
		<#list columns as item>
    		<if test="${item.attributeName} != null and ${item.attributeName} != ''">
    			 t.${item.columnName} = ${'#'}{${item.attributeName}},
    		</if>
        </#list>
		where
		1=1
		<#list columns as item>
    		<if test="${item.attributeName} != null and ${item.attributeName} != ''">
    			and  t.${item.columnName} = ${'#'}{${item.attributeName}}
    		</if>
        </#list>
	</update>
	<!--删除方法-->
	<delete id="delete" parameterType="${beanName}">
		delete from ${tableName} where 1=1
		<#list columns as item>
    		<if test="${item.attributeName} != null and ${item.attributeName} != ''">
    			and t.${item.columnName} = ${'#'}{${item.attributeName}}
    		</if>
        </#list>
	</delete>
</mapper>