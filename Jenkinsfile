pipeline{
agent any
tools{

maven "maven"

}

stages{

stage("build")
{
steps{
sh 'mvn -X package -DskipTests'
}
}

}
}
