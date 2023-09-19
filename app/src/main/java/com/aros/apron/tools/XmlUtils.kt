package com.aros.apron.tools

import com.shd.wpmz.xstream.SequenceFieldKeySorter
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.converters.basic.BooleanConverter
import com.thoughtworks.xstream.converters.reflection.FieldDictionary
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider

object XmlUtils {

    private val xStram=XStream(PureJavaReflectionProvider(FieldDictionary(SequenceFieldKeySorter()))).apply {
        autodetectAnnotations(true)
        aliasAttribute(null,"class")
        registerConverter(BooleanConverter.BINARY)
    }

    @JvmStatic
    fun kmlToXml(kml:Any):String{
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n${xStram.toXML(kml)}"
    }
}