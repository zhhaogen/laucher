package cn.zhg.launcher.parser;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParser;

import android.text.TextUtils;
import android.util.TypedValue;
import fix.android.content.res.AXmlResourceParser;
/**
 * 解析AndroidManifest.xml
 */
public class ManifestXmlParser
{
	private AXmlResourceParser parser;
	private StringBuilder sb;
	public ManifestXmlParser()
	{
		parser = new AXmlResourceParser();
		sb=new StringBuilder();
	}
	public String getXML()
	{
		return sb.toString();
	}
	public void parseApk(String apkFile) throws Exception
	{
		parseApk(apkFile==null?null:new File(apkFile));
	}

	public void parseApk(File apkFile) throws Exception
	{
		if (apkFile == null || !apkFile.exists())
		{
			return;
		}
		ZipFile zipFile = null;
		InputStream is = null;
		sb.setLength(0);
		try
		{
			zipFile = new ZipFile(apkFile);
			ZipEntry entry = zipFile.getEntry("AndroidManifest.xml");
			is = zipFile.getInputStream(entry);
			parser.open(is);
			while (true)
			{
				int type = parser.next();
				switch (type)
				{
				case XmlPullParser.END_DOCUMENT:
				{
					return;
				}
				case XmlPullParser.START_DOCUMENT:
				{
					sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
					continue;
				}
				case XmlPullParser.START_TAG:
				{
					sb.append("<");
					appendPrefix(sb,parser.getPrefix()); 
					sb.append(parser.getName()); 
					int namespaceCountBefore = parser
							.getNamespaceCount(parser.getDepth() - 1);
					int namespaceCount = parser
							.getNamespaceCount(parser.getDepth());
					for (int i = namespaceCountBefore; i != namespaceCount; ++i)
					{
						sb.append(" xmlns:").append(parser.getNamespacePrefix(i));
						sb.append("=\"").append(parser.getNamespaceUri(i)).append("\""); 
					} 
					for (int i = 0; i != parser.getAttributeCount(); ++i)
					{
						sb.append(" ");
						appendPrefix(sb,parser.getPrefix());  
						sb.append(parser.getAttributeName(i));
						sb.append("=\"");
						sb.append(getAttributeValue(parser, i));
						sb.append("\""); 
					}
					sb.append(">");
					continue;
				}
				case XmlPullParser.END_TAG:
				{ 
					sb.append("</");
					appendPrefix(sb,parser.getPrefix()); 
					sb.append(parser.getName());
					sb.append(">"); 
					break;
				}
				case XmlPullParser.TEXT:
				{
					sb.append(" ").append(parser.getText());
					break;
				}
				}
			}
		} finally
		{
			if (zipFile != null)
			{
				zipFile.close();
			}
			if (is != null)
			{
				is.close();
			}
		}
	}
	/**
	 * 添加属性前缀 
	 */
	private void appendPrefix(StringBuilder sb, String prefix)
	{ 
		if(TextUtils.isEmpty(prefix)){
			return;
		} 
		sb.append(prefix+":");
	}
	private   String getAttributeValue(AXmlResourceParser parser,int index) {
		int type=parser.getAttributeValueType(index);
		int data=parser.getAttributeValueData(index);
		if (type==TypedValue.TYPE_STRING) {
			return parser.getAttributeValue(index);
		}
		if (type==TypedValue.TYPE_ATTRIBUTE) {
			return String.format("?%s%08X",getPackage(data),data);
		}
		if (type==TypedValue.TYPE_REFERENCE) {
			return String.format("@%s%08X",getPackage(data),data);
		}
		if (type==TypedValue.TYPE_FLOAT) {
			return String.valueOf(Float.intBitsToFloat(data));
		}
		if (type==TypedValue.TYPE_INT_HEX) {
			return String.format("0x%08X",data);
		}
		if (type==TypedValue.TYPE_INT_BOOLEAN) {
			return data!=0?"true":"false";
		}
		if (type==TypedValue.TYPE_DIMENSION) {
			return Float.toString(complexToFloat(data))+
				DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		}
		if (type==TypedValue.TYPE_FRACTION) {
			return Float.toString(complexToFloat(data))+
				FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		}
		if (type>=TypedValue.TYPE_FIRST_COLOR_INT && type<=TypedValue.TYPE_LAST_COLOR_INT) {
			return String.format("#%08X",data);
		}
		if (type>=TypedValue.TYPE_FIRST_INT && type<=TypedValue.TYPE_LAST_INT) {
			return String.valueOf(data);
		}
		return String.format("<0x%X, type 0x%02X>",data,type);
	}
	private static String getPackage(int id) {
		if (id>>>24==1) {
			return "android:";
		}
		return "";
	}
	
	/////////////////////////////////// ILLEGAL STUFF, DONT LOOK :)
	
	private   float complexToFloat(int complex) {
		return (float)(complex & 0xFFFFFF00)*RADIX_MULTS[(complex>>4) & 3];
	}
	
	private static final float RADIX_MULTS[]={
		0.00390625F,3.051758E-005F,1.192093E-007F,4.656613E-010F
	};
	private static final String DIMENSION_UNITS[]={
		"px","dip","sp","pt","in","mm","",""
	};
	private static final String FRACTION_UNITS[]={
		"%","%p","","","","","",""
	};


}
