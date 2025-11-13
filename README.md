# Akarnoid Game - Object-Oriented Programing Project

## Author
Group 15 - Class 4
1. Trương Quang Hoành - 24020142
2. Trần Việt Bảo - 24020040
3. Đặng Nguyên Vũ - 24020367
4. Nguyễn Ngọc Ánh - 24020034

**Instructor**: Kiều Văn Tuyên - Vũ Đức Hiếu

**Semester**: HK1 - 2025-2026

## Description
This is classic Arkanoid game developed in Java as a final project for Object-Oriented Programing course. The project demonstrates the implementation of OOP principles and desing patterns.

### Key features:
1. The game is developed using Java 17+ with JavaFX for GUI
2. Implements core OOP principles: Encapsulation, Inheritance, Polymorphism and Abstraction.
3. Applies multiple design patterns: Singeton.
4. Includes sound effects, animations, and power-up systems.
5. Supports save/load game functionality and leaderboard system.
### Game mechanics
- Control a paddle to bounce a ball and destroy bricks
- Collect power-ups for special abilities
- Progress through multiple levels with increasing diffculty
- Score points and compete on the leaderboard

## UML Diagram
### Class Diagram
<img width="6166" height="10240" alt="Image" src="https://github.com/user-attachments/assets/9bc314a1-c710-4a2a-b399-f4f0905afbd3" />

### Design Patterns Implementation
### 1. Singleton Pattern
**Used in**: ``Brick, Ball, Entity``

**Purpose**: Ensure only one instance exists throughout the application.

### 2. Factory Method
**Used in**: ``*Manager``

**Purpose**: Creating cross-platform UI elements without coupling the client code to concrete UI classes.

### Installation
---
1. Clone the project from the repository
2. Open the project in the IDE
3. Run the project
## Usage
### Controls
| Key | Action |
|-----|--------|
| `←` | Move paddle left |
| `→` | Move paddle right |
| `SPACE` | Shoot laser |
|`ESC` | Pause game |
### How to play
1. **Start the game**: Move the indicator to "Start" and press Enter.
2. **Control the paddle**: Use arrow keys to move left and right.
3. **Destroy bricks and enimies**: Bounce the ball to hit and destroy bricks and enemies.
4. **Collect power-ups**: Catch falling power-ups for special abilities.
5. **Avoid losing the ball**: Keep the ball from falling below the paddle.
6. **Complete the level**: Destroy all destructible bricks to advance.
### Power-ups
| Icon | Name | Effect |
|------|------|--------|
| <img width="16" height="7" alt="Image" src="https://github.com/user-attachments/assets/1eee32f5-6258-4dbc-af20-be80212e9cdf" /> | E Capsule | Increases paddle width. |
| <img width="16" height="7" alt="Image" src="https://github.com/user-attachments/assets/25eeb75e-f085-4bb2-ab17-566c9a1a8bf9" /> | M Capsule | Changes your ball so it destroys all blocks it hits in its path. |
| <img width="16" height="7" alt="Image" src="https://github.com/user-attachments/assets/0705dc62-accb-4f6f-ac7b-8c03685ecc3d" /> | L Capsule | Gives your ship a gray appearance and gives it the ability to shoot lasers at blocks. |
| <img width="16" height="7" alt="Image" src="https://github.com/user-attachments/assets/fc51500d-45f7-4e4c-9ef4-fb1a48489a2d" /> | D Capsule | Collecting this will generate 8 balls.|
### Scoring System
- 10 points-each time the ball hits your scout ship.
- 30000; 80000 points-for destroying a boss.
- 0 points-for hitting a Gold Block
- 500 points for destroying a normal block.
- 0 points for destroying a Gray Block.
### Enemies
| Icon | Name | Effect |
|------|------|--------|
| <img width="14" height="14" alt="Image" src="https://github.com/user-attachments/assets/7c336fc7-c823-4c77-9ed7-75c192821ab8" /> | Mini-Saturns | These enemies look like small saturn shaped orbs. They help you because when you destroy them they have a large explosion. |
| <img width="16" height="16" alt="Image" src="https://github.com/user-attachments/assets/9debecce-c134-40ae-af13-48231c358f19" /> | Red Blobs | These enemies look like red blobs that change shape from a diamond to a circle constantly. |
| <img width="16" height="15" alt="Image" src="https://github.com/user-attachments/assets/13d5a0fe-6c99-4485-80d3-4d9098f09478" />| Bubble | These enemies look like bubbles!  When your ball hits them it will suck it up and place it in a new section of the puzzle. |
| <img width="14" height="13" alt="Image" src="https://github.com/user-attachments/assets/65942590-9712-4825-8197-e3219185928c" /> | Molecular Model | This enemy looks like a molecule.  When hit they will explode and release their three balls destroying any of your blocks in your way. One more hit from your ball will destroy the balls flying around.|
| <img width="15" height="17" alt="Image" src="https://github.com/user-attachments/assets/a56baaee-829c-48d6-8fa5-562ebbf3ea13" />| TriangleC | These thingss look like a Triangle rotating on one end of a circle. They do nothing except get in the way.
| <img width="16" height="16" alt="Image" src="https://github.com/user-attachments/assets/3c99d4f1-6346-4dca-822d-31176e277dd2" />| Infinitys | These look like an infinity symbol.  These things generate feilds of electricity that makes hard for you to get your ball past them.
## Bosses
### Giant Centipede
<img width="1152" height="1004" alt="Image" src="https://github.com/user-attachments/assets/d4fed1f0-6784-4359-813d-552e6a7c672a" />
This boss is rather easy hitting him anywhere causes damage.  You face him
first be careful that you do not get hit by the debris that he spits from
his parts.  Also watch out when he compacts into a little ball because he is
about to come straight at you.  Move out the way.  You face him first.

#### Doh's Face
<img width="1150" height="1009" alt="image" src="https://github.com/user-attachments/assets/c25aaa56-dbfb-4e99-980f-fa3087d133e4" />
This boss is tough unless you know where to hide when he fires at you.  He
will spit out Mini-Faces that will also shoot at you when they stop moving
and go up towards the top of the screen and to the lower right and left.  He
can realease 4 of them at a time you can take them out before they fire to
make it easier for you.  But they'll come back.  If all the faces do fire
hide in between the upper Mini-face and Doh's Face.  You'll survive but
youre ball will probably fall offscreen and you will lose.  For many of
the players this is the first stage they will lose.  Tryo to sneak in shots
at Doh's Face whenever possible and whenever he takes enough dammage the
mini faces will blow up and his face will turn around.  And he will fire
lasers at wherever you are.  Keep moving.  And use your ball to hit him some
more.  The more you hit the bigger his face will get.  He is an easy target
now but so are you.  But not as much as you think.  You shouldn't have any
trouble when his face gets big.

## Demo
### Screenshots
**Main Menu**
<img width="1151" height="1043" alt="image" src="https://github.com/user-attachments/assets/5a4a6c15-f38b-4387-b50c-fc87ff1d5731" />

**Gameplay**  
<img width="1148" height="1046" alt="image" src="https://github.com/user-attachments/assets/e785c389-9c77-409f-a517-78819229c115" />

**Power-ups in Action**  
<img width="1153" height="1006" alt="image" src="https://github.com/user-attachments/assets/092cb8ad-dd1d-476c-bd3c-f98f26d51550" />

**Leaderboard**  
<img width="1148" height="1006" alt="image" src="https://github.com/user-attachments/assets/f375ea00-c78a-4ad2-950b-add1b5b75d83" />

### Video Demo
Full gameplay available at: https://www.youtube.com/watch?v=VRjX2IPqAfM

## Future Improvements
1. **Additional game modes**
   - Time attack mode
   - Survival mode with endless levels
   - Co-op multiplayer mode

2. **Enhanced gameplay**
   - More power-up varieties (freeze time, shield wall, etc.)
   - Achievements system

3. **Technical improvements**
   - Migrate to LibGDX or JavaFX for better graphics
   - Add particle effects and advanced animations
   - Implement AI opponent mode
   - Add online leaderboard with database backend
---
## Technologies Used
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Core language |
| JavaFX | 19.0.2 | GUI framework |
| Maven | 3.9+ | Build tool |
| Python | 3.13 | Level processing |
## License
This project is developed for educational purposes only.
## Notes

- The game was developed as part of the Object-Oriented Programming with Java course curriculum.
- All code is written by group members with guidance from the instructor.
- Some assets (images, sounds) may be used for educational purposes under fair use.
- The project demonstrates practical application of OOP concepts and design patterns.
---
*Last updated: [12/11/2025]*
