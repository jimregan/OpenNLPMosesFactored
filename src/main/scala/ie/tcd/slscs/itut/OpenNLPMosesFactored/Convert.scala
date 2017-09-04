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

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.tools.lemmatizer.LemmatizerModel
import opennlp.tools.lemmatizer.LemmatizerME


object Convert extends App {

  if(args.length < 1) {
    throw new Exception("Specify language")
  }
  val lang = args(0)
  val type: String = if(args.size == 2) args(1) else "moses"
  
  def lc(a: Array[String], lang: String): Array[String] = {
    if(lang == "ga") {
      a.map{e => gaToLower(e)}
    } else {
      a.map{_.toLowerCase}
    }
  }
  
  def mkFactoredString(s: String, ftype: String, lang: String): String = {
    val in = s.split(" ")
    if(ftype == "moses") {
    
    } else {
      // OpenNMT
      val lowers = lc(in, lang)
    
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

