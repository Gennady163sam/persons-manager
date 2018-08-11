<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
  <html>
  <body>
  <h2>Import person</h2>
    <table border="1">
      <tr bgcolor="#9acd32">
        <th style="text-align:left">Id</th>
        <th style="text-align:left">Name</th>
        <th style="text-align:left">Parent Id</th>
        <th style="text-align:left">Attribute</th>
      </tr>
      <xsl:for-each select="company/person">
      <tr>
        <td><xsl:value-of select="@id"/></td>
        <td><xsl:value-of select="@name"/></td>
        <td><xsl:value-of select="@parentId"/></td>
        <td>
            <xsl:for-each select="attributes/entry">
               <xsl:value-of select="key/@name"/>  - 
               <xsl:value-of select="value"/>
               <br/>
            </xsl:for-each>
        </td>
      </tr>
	  </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>