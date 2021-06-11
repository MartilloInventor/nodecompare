package org.webdev

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.beans.BeanProperty
import java.io.{File, FileInputStream}

object NodeCompare {
  def main(args: Array[String]): Unit = {
    val filename1 = "src/main/data/config1.yaml"
    val filename2 = "src/main/data/config2.yaml"
    val input1 = new FileInputStream(new File(filename1))
    val input2 = new FileInputStream(new File(filename2))
    val yaml = new Yaml(new Constructor(classOf[TreeDefinition]))
    val root1 = yaml.load(input1).asInstanceOf[TreeDefinition]
    println(root1)
    val root2 = yaml.load(input2).asInstanceOf[TreeDefinition]
    println(root2)
    val terminals = convertRootToTree(root1)
    if(convertCompareRootToTree(root2, terminals))
      println("Alike")
    else
      println("Not alike")
    /*
    if(tree1 != null)
      if(tree1.nodeLike(tree2))
        println("The two trees are similar")
      else
        println("The true trees are dissimilar")
     */
  }
  def convertRootToTree(root: TreeDefinition) : List[Int] = {
    //var tree: TreeNode = new TreeNode(root.nodedefinitions(0).name)
    var terms: List[Int] = List[Int]()
    for(n <- root.nodedefinitions) {
      //tree.finish(n)
      if(n.left == null && n.right == null)
        terms = terms ++ List(n.value.toInt)
    }
    terms
  }

  def convertCompareRootToTree(root: TreeDefinition, terms: List[Int]) :Boolean = {
    //var tree: TreeNode = new TreeNode(root.nodedefinitions(0).name)
    var i: Int = 0
    for(n <- root.nodedefinitions) {
      //tree.finish(n)
      if(n.left == null && n.right == null)
        if(terms(i) == n.value.toInt) {
          i = i+1
        }
    }
    i == terms.length
  }
}

/**
 * With the Snakeyaml Constructor approach shown in the main method,
 * this class must have a no-args constructor.
 */
class NodeDefinition {
  @BeanProperty var name = ""
  @BeanProperty var value = ""
  @BeanProperty var left = ""
  @BeanProperty var right = ""
  override def toString: String = s"NodeDefinition: name: $name, value: $value, left: $left, right: $right"
}

class TreeDefinition {
  @BeanProperty var name = ""
  @BeanProperty var nodedefinitions: Array[NodeDefinition] = Array[NodeDefinition]()
  override def toString: String = s"Tree Definition: name $name:\n"+conCatNodeDefs(nodedefinitions)
  def conCatNodeDefs(defs: Array[NodeDefinition]): String = {
    var s = ""
    for(d <- defs) {
      s = s + d + "\n"
    }
    s
  }
}


