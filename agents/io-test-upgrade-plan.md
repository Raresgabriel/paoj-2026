# IOTest Upgrade Plan for Laboratory 06

## Goal
- IOTest should be able to run all parts of the laboratory (exercise1, all parts of exercise2, and optionally exercise3 if testable).
- Display grading summary: points won, bonus status, and eligibility for max grade.

## Features
- Detect and run all partA/partB/partC folders for each exercise.
- For each exercise, display:
  - Points won per part
  - Total points for the exercise
  - Whether the exercise is main or bonus
- At the end, display:
  - Total points for the laboratory
  - Whether the student is eligible for the 0.5% bonus (if exercise3 is completed and at least 8 exercises are done)
  - Whether the student can reach the maximum grade even if the grade is not rounded
- Output clear feedback for each part and overall lab status.
- **Laboratory requirements will be found in the corresponding `laboratory0x/Readme.md`, in a dedicated section. IOTest should reference this section for grading and requirements.**

## Implementation Steps
1. Update IOTest to scan all exercises in the laboratory folder.
2. For each exercise, detect all part folders and run tests as usual.
3. Track and display points per part and per exercise.
4. Add logic to detect completion of the bonus exercise and display bonus eligibility.
5. Summarize results at the end, including bonus and max grade eligibility.
6. Reference the requirements section in `laboratory0x/Readme.md` for each laboratory.

## Example Output
```
Exercise 1: 3/3 points (main)
Exercise 2: 3/3 points (main)
Exercise 3: completed (bonus)

Total: 6/6 points
Bonus: eligible (0.5% will be added to final grade)
Max grade: possible even if not rounded by using bonus points
```
