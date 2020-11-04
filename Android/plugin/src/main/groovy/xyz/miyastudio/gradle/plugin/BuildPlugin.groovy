package xyz.miyastudio.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extension = project.getExtensions().create('myExt',MyExtension)
        project.afterEvaluate {
            println("Hello from " + extension.toString())
        }
        // 功能1，创建一个名称为hello的gradle task
        project.task('hello') << {
                println 'Hello from the GreetingPlugin'
                project.android.applicationVariants.all { variant ->
                    println 'variant: '+variant.name
                    variant.outputs.each { output ->
                        println 'outputFile: '+output.outputFile
                    }
                }
        }
        // 功能2，对编译后的apk进行一些后处理操作
        BuildExtension extension2 = project.getExtensions().create("resign", ResignPluginExtension.class)

        project.android.applicationVariants.all { variant ->
            variant.assemble.doLast {
                println("resign directory: " + extension2.signDirectory)
                if (extension2.signDirectory == null) {
                    return
                }

                final File signDirectory = new File(extension2.signDirectory)
                if (!signDirectory.exists()) {
                    return
                }
                if (!signDirectory.isDirectory()) {
                    return
                }

//                println 'variant1: '+variant.name
                variant.outputs.each { output ->
//                    println 'outputFile1: '+output.outputFile
                    println('resign ' + output.outputFile + " start")
                    JavaUtils2.resign(signDirectory, output.outputFile)
                    println('resign ' + output.outputFile + " end")
                }
            }
        }
        println 'Hello,World!'
    }

}