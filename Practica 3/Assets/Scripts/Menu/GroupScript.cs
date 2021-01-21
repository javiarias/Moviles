using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GroupScript : MonoBehaviour
{
    LevelPackage[] levelPackages;

    //Arrays de botones
    MenuButton[] groupButtons;
    LevelButton[] levelButtons;
    
    //Prefabs
    public MenuButton groupButton;
    public LevelButton levelButton;

    //Posicion de containers
    public Transform groupContainer;
    public Transform levelContainer;

    //Para la selección de niveles
    public GameObject levelSelection;
    public Text levelSelectionTitle;

    // Start is called before the first frame update
    void Start()
    {
        levelPackages = GameManager.Instance()._levelPacks;
        groupButtons = new MenuButton[levelPackages.Length];

        for (int i = 0; i < groupButtons.Length; i++)
        {
            groupButtons[i] = CreateGroupButton(levelPackages[i], i);
        }
    }

    public MenuButton CreateGroupButton(LevelPackage levelPackage, int i)
    {
        MenuButton butt = Instantiate(groupButton, groupContainer);

        SpriteState spriteState = new SpriteState();
        spriteState.pressedSprite = levelPackage._pressedButton;

        butt.setUp(levelPackage._button, spriteState, levelPackage._packName, this, i);
        return butt;
    }

    public void ShowLevels(int i)
    {
        //Se desactiva el canvas de grupos de niveles
        groupContainer.gameObject.SetActive(false);

        //Se activa el canvas del grupo elegido, y se pone el texto
        levelSelection.SetActive(true);
        levelSelectionTitle.text = levelPackages[i]._packName;

        //Creación de botones, ya que se empieza con el menu sin ellos
        int totalLevels = levelPackages[i]._levels.Length;
        levelButtons = new LevelButton[totalLevels];

        for (int j = 0; j < levelButtons.Length; j++)
        {
            levelButtons[j] = CreateLevelButton(j, levelPackages[i]._colorScheme, i);
        }
    }

    public LevelButton CreateLevelButton(int number, Color colorScheme, int pack)
    {
        LevelButton butt = Instantiate(levelButton, levelContainer);

        int levelComp = GameManager.Instance().getLevelCompleted(pack);

        bool done = number < levelComp;
        bool available = number <= levelComp;

        butt.setUp(available, number + 1, colorScheme, pack, done);
        return butt;
    }
}
