package xyz.miyastudio.gradle.plugin

import org.gradle.api.Project

Plugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        println 'Hello,World!'
    }

}