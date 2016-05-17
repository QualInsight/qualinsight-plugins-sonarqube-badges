<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

  <xsl:output indent="no" omit-xml-declaration="no" include-content-type="yes" />
  <xsl:param name="IS_BLINKING_BADGE" select="'false'" />

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="animate">
    <xsl:choose>
      <xsl:when test="$IS_BLINKING_BADGE='true'">
        <xsl:copy>
          <xsl:copy-of select="@*" />
          <xsl:apply-templates />
        </xsl:copy>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="text()[not(string-length(normalize-space()))]" />
  <xsl:template match="text()[string-length(normalize-space()) > 0]">
    <xsl:value-of select="translate(.,'&#xA;&#xD;', '  ')" />
  </xsl:template>
</xsl:transform>