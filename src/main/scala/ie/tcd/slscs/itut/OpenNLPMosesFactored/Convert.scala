/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Jim O'Regan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ie.tcd.slscs.itut.OpenNLPMosesFactored

import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.lemmatizer.DictionaryLemmatizer
import opennlp.tools.lemmatizer.LemmatizerModel
import opennlp.tools.lemmatizer.LemmatizerME
import scala.io.Source
import java.io._
import java.nio.charset.Charset

object Converter {
  val enlembin: InputStream = getClass.getResourceAsStream("/en-lemmatizer.dict")
  val enposbin: InputStream = getClass.getResourceAsStream("/en-pos-maxent.bin")
  val galembin: InputStream = getClass.getResourceAsStream("/ga-lemmatizer.bin")
  val gaposbin: InputStream = getClass.getResourceAsStream("/ga-pos-maxent.bin")
  
  val enlem = new DictionaryLemmatizer(enlembin)
  val enpos = new POSTaggerME(new POSModel(enposbin))
  val galem = new LemmatizerME(new LemmatizerModel(galembin))
  val gapos = new POSTaggerME(new POSModel(gaposbin))

  def lc(a: Array[String], lang: String): Array[String] = {
    if(lang == "ga") {
      a.map{e => gaToLower(e)}
    } else {
      a.map{_.toLowerCase}
    }
  }
  
  def mkFactoredString(s: String, ftype: String, lang: String): String = {
    val onmt_splitter = "￨"
    val in = if(ftype == "moses") {
        s.split(" ")
      } else {
        s.split(" ").map{e => e.split(onmt_splitter)(0)}
      }
    val onmtcase = if(ftype == "moses") {
        null
      } else {
        s.split(" ").map{e => e.split(onmt_splitter)(1)}
      }
    val pos = if(lang == "ga") gapos else enpos
    val lem = if(lang == "ga") galem else enlem
    val tags = pos.tag(in)
    val lemma = lem.lemmatize(in, tags)
    if(ftype == "moses") {
      in.zip(lemma).zip(tags).map{e => List(e._1._1, e._1._2, e._2).mkString("|")}.mkString(" ") + "\n"
    } else {
      // OpenNMT
      in.zip(onmtcase).zip(tags).map{e => List(e._1._1, e._1._2, e._2).mkString(onmt_splitter)}.mkString(" ") + "\n"
    }
  }

  def isIrishUpperVowel(c: Char): Boolean = c match {
    case 'A' | 'E' | 'I' | 'O' | 'U' | 'Á' | 'É' | 'Í' | 'Ó' | 'Ú'  => true
    case _ => false
  }
  def gaToLower(s: String): String = {
    if(s == "") {
      ""
    }
    if(s.length == 1) {
      s.toLowerCase
    }
    val first = s.charAt(0)
    val second = s.charAt(1)
    if((first == 'n' || first == 't') && isIrishUpperVowel(second)) {
      first + "-" + s.substring(1).toLowerCase
    } else {
      s.toLowerCase
    }
  }
}

object Convert extends App {
  if(args.length < 1) {
    throw new Exception("No filename specified")
  }
  val filename = args(0)
  val outputname = filename + "-out"
  val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputname), Charset.forName("UTF-8")))
  
  if(args.length < 2) {
    throw new Exception("No language specified")
  }
  val lang = args(1)
  val f_type: String = if(args.size == 3) args(2) else "moses"

  for (line <- Source.fromFile(filename).getLines) {
    writer.write(Converter.mkFactoredString(line, f_type, lang))
  }
  writer.close
  System.exit(0)
 
}

