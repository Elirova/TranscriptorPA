<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>DSSA</title>
		<link href="./DSSA.css" rel="stylesheet" type="text/css" />
	</head>
	<body id="bopa" xml:lang="es-ES">
		<div class='header'>
			<p class="tipo_iniciativa center">DIARIO DE SESIONES DEL PARLAMENTO DE ANDALUCÍA</p>
			<table class="info">
				<tr>
					<td class="left columns3">Núm. <xsl:value-of select="diario_sesion_pa/numero_diario"/></td>
					<td class="center columns3"><xsl:value-of select="diario_sesion_pa/legislatura"/> LEGISLATURA</td>
					<td class="right columns3"><xsl:value-of select="diario_sesion_pa/fecha/dia"/> de <xsl:value-of select="diario_sesion_pa/fecha/mes"/> de <xsl:value-of select="diario_sesion_pa/fecha/anio"/></td>
				</tr>
			</table>
		</div>
		<div class='footer'>
	    		<p>Pág. <span class="page" /></p>
		</div>
		<div class="center banner"><img src="./images/title.png"/></div>
		<p class="organo"><xsl:value-of select="diario_sesion_pa/organo"/></p>		
		<table class="info">
			<tr>
				<td class="left columns3">Núm. <xsl:value-of select="diario_sesion_pa/numero_diario"/></td>
				<td class="center columns3"><xsl:value-of select="diario_sesion_pa/legislatura"/> LEGISLATURA</td>
				<td class="right columns3"><xsl:value-of select="diario_sesion_pa/fecha/dia"/> de <xsl:value-of select="diario_sesion_pa/fecha/mes"/> de <xsl:value-of select="diario_sesion_pa/fecha/anio"/></td>
			</tr>
		</table>

		<p class="presidencia">Presidencia: <xsl:value-of select="diario_sesion_pa/presidente"/></p>
		<xsl:if test="diario_sesion_pa/numero_sesion"><p class="numero_sesion">Sesión plenaria número <xsl:value-of select="diario_sesion_pa/numero_sesion"/></p>
		<p class="fecha">celebrada el <xsl:value-of select="diario_sesion_pa/fecha/dia"/> de <xsl:value-of select="diario_sesion_pa/fecha/mes"/> de <xsl:value-of select="diario_sesion_pa/fecha/anio"/></p></xsl:if>
		<div class="line"/>	

		<p class="sumario">ORDEN DEL DÍA</p>
		<xsl:apply-templates select="diario_sesion_pa/desarrollo" mode="orden"/>
		<div class="line"/>	
		<div class="page-break"></div>

		<p class="sumario">SUMARIO</p>
		<xsl:apply-templates select="diario_sesion_pa/desarrollo" mode="sumario"/>
		<div class="line"/>	
		<div class="page-break"></div>

		<xsl:apply-templates select="diario_sesion_pa/desarrollo" mode="total"/>
	</body>
</html>

</xsl:template>

<!-- TEMPLATE PARA EL ORDEN DEL DIA -->
<xsl:key name="iniciativa_por_tipo" match="iniciativa" use="tipo_iniciativa" />

<xsl:template match="desarrollo" mode="orden">
	<xsl:for-each select=".//iniciativa[count(. | key('iniciativa_por_tipo', tipo_iniciativa)[1]) = 1]">
		<xsl:sort select="tipo_iniciativa" />
		<p class="tipo_iniciativa"><xsl:value-of select="tipo_iniciativa" /></p>
		<xsl:for-each select="key('iniciativa_por_tipo', tipo_iniciativa)">
			<xsl:if test="@Aparece_en_orden_del_dia='Si'">
				<p class="extracto">
					<xsl:if test="numero_expediente"><xsl:value-of select="numero_expediente"/>. </xsl:if>
					<xsl:value-of select="extracto" />
					<xsl:if test="proponentes">, formulada por <xsl:value-of select="proponentes"/>.</xsl:if>
				</p>
			</xsl:if>
		</xsl:for-each>
	</xsl:for-each>
</xsl:template>

<!-- TEMPLATE PARA EL SUMARIO -->
<xsl:template match="desarrollo" mode="sumario">
	<xsl:for-each select=".//iniciativa[count(. | key('iniciativa_por_tipo', tipo_iniciativa)[1]) = 1]">
		<xsl:sort select="tipo_iniciativa" />
		<p class="tipo_iniciativa"><xsl:value-of select="tipo_iniciativa" /></p>
		<xsl:for-each select="key('iniciativa_por_tipo', tipo_iniciativa)">
			<p class="extracto">
				<xsl:if test="numero_expediente"><xsl:value-of select="numero_expediente"/>. </xsl:if>
				<xsl:value-of select="extracto" />
			</p>
			<xsl:if test="intervienen">
			<div class="intervienen">
				Intervienen:<br/>
				<xsl:call-template name="br">
			    		<xsl:with-param name="text" select="intervienen"/>
				</xsl:call-template></div></xsl:if>
			<xsl:if test="votacion"><p class="votacion"><xsl:value-of select="votacion"/></p></xsl:if>
		</xsl:for-each>
	</xsl:for-each>
</xsl:template>


<!-- TEMPLATE PARA LAS INICIATIVAS EXTENDIDAS -->
<xsl:template match="desarrollo" mode="total">
	<xsl:for-each select=".//iniciativa">
		<p class="extracto_extendido">
			<xsl:if test="numero_expediente"><xsl:value-of select="numero_expediente"/>. </xsl:if>
			<xsl:value-of select="extracto" /> 
		</p>

		<xsl:for-each select="intervencion">
			<p class="interviniente"><xsl:value-of select="interviniente"/></p>
			<xsl:for-each select="discurso/parrafo">
				<p class="parrafo"><xsl:value-of select="."/></p>
			</xsl:for-each>
		</xsl:for-each>

		<div class="line"/>	
		<div class="page-break"></div>
	</xsl:for-each>
</xsl:template>

<xsl:template match="desarrollo" mode="antiguo">
	<xsl:for-each select=".//iniciativa">
		<p class="sumtitulo1 titulo-seccion"><xsl:value-of select="../../titulo"/></p>
		<p class="Contestaciones_sumtitulo3 ParaOverride-3"><xsl:value-of select="../titulo"/></p>
		<p class="extracto"><xsl:if test="numero_expediente"><span class="numero_expediente"><xsl:value-of select="numero_expediente"/><span>, </span></span></xsl:if>
		<xsl:value-of select="extracto"/></p>
		<div class="cuadro_tramitacion">
		<xsl:call-template name="br">
	    		<xsl:with-param name="text" select="cuadro_tramitacion"/>
		</xsl:call-template>
		</div>
		<xsl:for-each select="discurso/parrafo">
			<p class="discurso_parrafo"><xsl:value-of select="."/></p>
		</xsl:for-each>
		<div class="firma">
		<xsl:call-template name="br">
	    		<xsl:with-param name="text" select="firma"/>
		</xsl:call-template>
		</div>
		<div class="page-break"></div>
	</xsl:for-each>
</xsl:template>

<!-- TEMPLATE PARA SEPARAR EN VARIAS LINEAS -->
<xsl:template name="br">
    <xsl:param name="text"/>
    <xsl:choose>
        <xsl:when test="contains($text,'&#xa;')">
            <xsl:value-of select="substring-before($text,'&#xa;')"/>
            <br/>
            <xsl:call-template name="br">
                <xsl:with-param name="text">
                    <xsl:value-of select="substring-after($text,'&#xa;')"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="$text"/>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>
</xsl:stylesheet>
