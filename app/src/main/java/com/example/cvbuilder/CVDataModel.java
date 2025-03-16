package com.example.cvbuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CVDataModel implements Serializable {
        private String profilePicturePath;
        private String name;
        private String email;
        private String phone;
        private String address;
        private String dob;
        private String summary;

        // Education
        private List<Education> educationList;

        // Experience
        private List<Experience> experienceList;

        // Certifications
        private List<String> certifications;

        // References
        private List<Reference> references;

        public void CVDataModel() {
            this.educationList = new ArrayList<>();
            this.experienceList = new ArrayList<>();
            this.certifications = new ArrayList<>();
            this.references = new ArrayList<>();
        }

        // Inner classes for complex data
        public static class Education implements Serializable {
            private String degree;
            private String institution;
            private String year;
            private String grade;

            public Education(String degree, String institution, String year, String grade) {
                this.degree = degree;
                this.institution = institution;
                this.year = year;
                this.grade = grade;
            }

            // Getters and setters
            public String getDegree() { return degree; }
            public void setDegree(String degree) { this.degree = degree; }

            public String getInstitution() { return institution; }
            public void setInstitution(String institution) { this.institution = institution; }

            public String getYear() { return year; }
            public void setYear(String year) { this.year = year; }

            public String getGrade() { return grade; }
            public void setGrade(String grade) { this.grade = grade; }
        }

        public static class Experience implements Serializable {
            private String position;
            private String company;
            private String duration;
            private String description;

            public Experience(String position, String company, String duration, String description) {
                this.position = position;
                this.company = company;
                this.duration = duration;
                this.description = description;
            }

            // Getters and setters
            public String getPosition() { return position; }
            public void setPosition(String position) { this.position = position; }

            public String getCompany() { return company; }
            public void setCompany(String company) { this.company = company; }

            public String getDuration() { return duration; }
            public void setDuration(String duration) { this.duration = duration; }

            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
        }

        public static class Reference implements Serializable {
            private String name;
            private String position;
            private String contact;

            public Reference(String name, String position, String contact) {
                this.name = name;
                this.position = position;
                this.contact = contact;
            }

            // Getters and setters
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }

            public String getPosition() { return position; }
            public void setPosition(String position) { this.position = position; }

            public String getContact() { return contact; }
            public void setContact(String contact) { this.contact = contact; }
        }

        // Getters and setters
        public String getProfilePicturePath() { return profilePicturePath; }
        public void setProfilePicturePath(String profilePicturePath) { this.profilePicturePath = profilePicturePath; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getDob() { return dob; }
        public void setDob(String dob) { this.dob = dob; }

        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }

        public List<Education> getEducationList() { return educationList; }
        public void addEducation(Education education) { this.educationList.add(education); }

        public List<Experience> getExperienceList() { return experienceList; }
        public void addExperience(Experience experience) { this.experienceList.add(experience); }

        public List<String> getCertifications() { return certifications; }
        public void addCertification(String certification) { this.certifications.add(certification); }

        public List<Reference> getReferences() { return references; }
        public void addReference(Reference reference) { this.references.add(reference); }
    }