import com.android.build.api.dsl.ApplicationExtension
import gradleplugins.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies


//Before uncomment lines, add appropriate libraries to lib.versions.toml
class ComposeApplicationGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(project.pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidCompose(this)
                dependencies {
                    //add("implementation", libs.findLibrary("activity.compose").get())
                }
            }
        }
    }
}