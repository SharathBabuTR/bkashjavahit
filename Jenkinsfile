pipeline{
agent any
tools{

maven "maven"

}

stages{

stage('Clone sources') {
        git url: 'https://github.com/jfrogdev/project-examples.git'
    }

stage("test")

{
steps{
sh 'mv test'
}
}
stage("build")
{
steps{
sh 'mv package'
}
}

}
}