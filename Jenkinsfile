pipeline{
agent any
tools{

maven "maven"

}

stages{


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
