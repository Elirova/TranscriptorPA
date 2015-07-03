<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

<xsl:template match="/">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>bopa</title>
		<link href="./BOPA.css" rel="stylesheet" type="text/css" />
	</head>
	<body id="bopa" xml:lang="es-ES">
		<div class='header'>
			<p class="sumtitulo1 titulo-seccion">BOLETÍN OFICIAL DEL PARLAMENTO DE ANDALUCÍA</p>
			<table class="info">
				<tr>
					<td class="left columns3">Núm. <xsl:value-of select="bopa/numero_bopa"/></td>
					<td class="center columns3"><xsl:value-of select="bopa/legislatura"/> LEGISLATURA</td>
					<td class="right columns3"><xsl:value-of select="bopa/fecha/dia"/> de <xsl:value-of select="bopa/fecha/mes"/> de <xsl:value-of select="bopa/fecha/anio"/></td>
				</tr>
			</table>
		</div>
		<div class='footer'>
	    		<p>Pág. <span class="page" /></p>
		</div>
		<div class="center banner"><img src="./images/title.png"/></div>
		<table class="info">
			<tr>
				<td class="left columns3">Núm. <xsl:value-of select="bopa/numero_bopa"/></td>
				<td class="center columns3"><xsl:value-of select="bopa/legislatura"/> LEGISLATURA</td>
				<td class="right columns3"><xsl:value-of select="bopa/fecha/dia"/> de <xsl:value-of select="bopa/fecha/mes"/> de <xsl:value-of select="bopa/fecha/anio"/></td>
			</tr>
		</table>

		<p class="sumario">SUMARIO</p>
		<xsl:for-each select="bopa/desarrollo/seccion">
			<p class="sumtitulo1 titulo-seccion"><xsl:value-of select="titulo"/></p>
			<xsl:apply-templates select=".//subseccion" mode="sumario"/>
	    	</xsl:for-each>
		<div class="page-break"></div>

		<xsl:for-each select="bopa/desarrollo">
			<xsl:apply-templates select=".//seccion" mode="total"/>
	    	</xsl:for-each>

	</body>
</html>

</xsl:template>

<!-- TEMPLATE PARA EL SUMARIO -->
<xsl:template match="subseccion" mode="sumario">
	<xsl:if test="parent::seccion"><p class="sumtitulo2 ParaOverride-2"><xsl:value-of select="titulo"/></p></xsl:if>
	<xsl:if test="parent::subseccion"><p class="Contestaciones_sumtitulo3 ParaOverride-3"><xsl:value-of select="titulo"/></p></xsl:if>
		
		<xsl:for-each select="iniciativa">
			<div class="list">
				<xsl:if test="numero_expediente"><xsl:value-of select="numero_expediente"/><span class="sumtexto1-cursiva _idGenCharOverride-1">, </span></xsl:if>
				<xsl:value-of select="extracto"/>
			</div>
		</xsl:for-each>
		
</xsl:template>


<!-- TEMPLATE PARA LAS INICIATIVAS EXTENDIDAS -->
<xsl:template match="seccion" mode="total">
	<xsl:for-each select="subseccion/iniciativa">
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
