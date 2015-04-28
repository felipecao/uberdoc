package sample

class Person {

    String firstName
    String lastName
    List<String> nickNames

    static transients = ['fullName']
    static hasMany = [nickNames: String, pods: Pod]

    String getFullName() {
        return firstName + " " + lastName
    }
}
