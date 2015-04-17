 ROOT_DIR = ".."
 FINDBUGS_HOME =  System.getenv().get('FINDBUGS_HOME')
 MAVEN_HOME = System.getenv().get('M2_HOME')
 REPORT_DIR = "./findbugs_report"



File reportDir = new File(REPORT_DIR)
//reportDir.deleteOnExit()
reportDir.mkdir()

def prj = new Project(new File(ROOT_DIR))
def modules = prj.getModules();

Findbugs findbugs = new Findbugs(FINDBUGS_HOME, REPORT_DIR)

modules.each{m ->
    findbugs.check(m);
    //println(m.name)
}

class Findbugs{
    String home
    String outputDir

    Findbugs(String home, String outputDir){
       this.home = home
        this.outputDir = outputDir
    }


    //println "${p.text}"

    def check(Module module){

        String c = new String(home +File.separator+"bin"+File.separator+"findbugs.bat -textui -output  "+outputDir+File.separator+module.getFindbugsOutputFileName()+ " " + module.getClassesDirPath())
println(c)
        Process p = c.execute()
       //println "${p.text}"


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

}
class Subversion{

}





//Process p="D:\\program\\findbugs-3.0.1\\bin\\findbugs.bat -textui -output a.xml D:\\work\\pop-admin-seller-yddms\\pop-admin-seller-dao\\target\\classes,D:\\work\\pop-admin-seller-yddms\\pop-admin-seller-domain\\target\\classes".execute()
//println "${p.text}"


//mvn dependency:list