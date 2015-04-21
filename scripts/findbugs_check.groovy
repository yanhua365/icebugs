ROOT_DIR = ".."
FINDBUGS_HOME =  System.getenv().get('FINDBUGS_HOME')
MAVEN_HOME = System.getenv().get('M2_HOME')
REPORT_DIR = "./findbugs_report"
SUBVERSION_HOME = System.getenv().get('SUBVERSION_HOME')


File reportDir = new File(REPORT_DIR)
//reportDir.deleteOnExit()
if(!reportDir.exists()) reportDir.mkdir()

def prj = new Project(new File(ROOT_DIR))
def modules = prj.getModules();

Findbugs findbugs = new Findbugs(FINDBUGS_HOME, REPORT_DIR)
Subversion subversion = new Subversion(SUBVERSION_HOME, REPORT_DIR)

modules.each{m ->
    subversion.diff(m)
}

//TODO 检查一下是否有变化，过滤出有变化的module给下面的findbugs用

modules.each{m ->
    findbugs.check(m)
}

class Findbugs{
    String home
    String outputDir

    Findbugs(String home, String outputDir){
        this.home = home
        this.outputDir = outputDir
    }

    def check(Module module){
        String c = new String(home +File.separator+"bin"+File.separator+"findbugs.bat -textui -output  "+outputDir+File.separator+module.getFindbugsOutputFileName()+ " " + module.getClassesDirPath())
        println(c)
        Process p = c.execute()

        def outputStream = new StringBuffer();
        p.waitForProcessOutput(outputStream, System.err)
        println "${outputStream}"
    }

}

class Project{
    File dir;

    def modules = []

    Project(File dir){
        this.dir = dir;
    }

    def getModules(){
        def results = []
        def pom = new File(dir,"pom.xml")

        def xml = new XmlParser().parse(pom)
        def ms = xml.modules.module;
        for(int i = 0; i < ms.size(); i++){
            Module m = new Module(this, ms[i].text())
            results << m
        }
        return results
    }
}

class Module{
    String name
    Project project

    Module(project, name){
        this.name = name
        this.project  = project
    }

    def getFindbugsOutputFileName(){
        return this.name+".xml"
    }

    def getClassesDirPath(){
        new File(project.dir, name + File.separator + "target" + File.separator + "classes").getAbsolutePath()
    }

    def getSrcDirPath(){
        new File(project.dir, name + File.separator + "src" + File.separator + "main" + File.separator + "java").getAbsolutePath()
    }

}
class Subversion{
    String home;
    String outputDir;
    Subversion(String home,String outputDir){
        this.home = home;
        this.outputDir = outputDir
    }

    def diff(Module module){
//        String c = new String(home +File.separator+"bin"+File.separator+"svn.exe diff --xml --summarize "+ module.getSrcDirPath() + ">" + outputDir + File.separator + module.getSvnOutputFileName())
        String c = new String(home +File.separator+"bin"+File.separator+"svn diff "+ module.getSrcDirPath())
        println(c)
        Process p = c.execute()

        def outputStream = new StringBuffer();
        p.waitForProcessOutput(outputStream, System.err)
        println "${outputStream}"
    }
}

//Process p="D:\\program\\findbugs-3.0.1\\bin\\findbugs.bat -textui -output a.xml D:\\work\\pop-admin-seller-yddms\\pop-admin-seller-dao\\target\\classes,D:\\work\\pop-admin-seller-yddms\\pop-admin-seller-domain\\target\\classes".execute()
//println "${p.text}"
//mvn dependency:list
