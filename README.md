# FileSort

A rule-based file organization tool that automatically sorts and manages files across directories. Built with an extensible design so new rules can be added over time without changing the core system.

---

## Overview
FileSort scans directories (including nested folders) and organizes files based on configurable rules such as file type and extensions. The system is designed to evolve—new rules can be introduced as new file patterns and use cases come up.

---

## Features
- Recursive directory traversal  
- Rule-based file sorting system  
- Supports grouping by file type (images, documents, spreadsheets, etc.)  
- Extensible architecture for adding new rules  
- Safe handling of unmatched files (files remain in place if no rule applies)  
- Duplicate-safe file movement (automatic renaming)  
- Logging of file operations for traceability  
- Rollback support to undo previous file movements  

---

## Current Rules
- Images → `png`, `jpg`, `jpeg`  
- Spreadsheets → `csv`, `xlsx`  
- Resumes → `pdf`, `docx`, contains resume in filename

---

## How It’s Evolving
The project is actively being expanded by adding new rules and improving flexibility. Planned improvements include:
- Filename-based classification (e.g., detecting "resume", "invoice")  
- Enhanced duplicate handling  
- More dynamic and configurable rule definitions  
- Improved CLI controls (e.g., dry-run mode, configurable limits)  

---

## Example Rule
```java
new Rule(List.of("csv", "xlsx"), "Sheets");