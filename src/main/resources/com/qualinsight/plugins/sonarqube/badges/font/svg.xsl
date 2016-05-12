<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

  <xsl:output indent="no" omit-xml-declaration="no" include-content-type="yes" />

  <xsl:variable name="INPUT_FONT_FAMILY">'Dialog'</xsl:variable>
  <xsl:param name="OUTPUT_FONT_FAMILY" />

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@style">
    <xsl:attribute name="style">
      <xsl:choose>
        <xsl:when test="contains(.,$INPUT_FONT_FAMILY)">
          <xsl:value-of select="substring-before(.,$INPUT_FONT_FAMILY)" />
          <xsl:value-of select="$OUTPUT_FONT_FAMILY" />
          <xsl:value-of select="substring-after(.,$INPUT_FONT_FAMILY)" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="." />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
  </xsl:template>

  <xsl:template match="text()[not(string-length(normalize-space()))]" />
  <xsl:template match="text()[string-length(normalize-space()) > 0]">
    <xsl:value-of select="translate(.,'&#xA;&#xD;', '  ')" />
  </xsl:template>
</xsl:transform>