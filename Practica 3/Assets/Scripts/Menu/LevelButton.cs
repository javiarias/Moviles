using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class LevelButton : MonoBehaviour
{
    // Componentes del botón como tal
    public Image image;
    public Image lockImage;
    public Button button;
    public Text text;

    int levelNumber;
    int pack;

    //Color
    private Color notCompletedColor = new Color(255, 255, 255);
    private Color notCompletedTextColor = new Color(0, 0, 0);
    private Color completedTextColor = new Color(255, 255, 255);
    private Color blockedColor = new Color(255, 255, 255);
    private Color blockedLockColor = new Color(0,0,0);

    public void setUp(bool blocked, int i, Color color, int pack_, bool done)
    {
        pack = pack_;
        levelNumber = i;

        image.gameObject.SetActive(true);

        if (blocked)
        {
            image.color = blockedColor;
            lockImage.color = blockedLockColor;
            lockImage.gameObject.SetActive(true);
        }
        else
        {
            text.text = i.ToString();
            text.gameObject.SetActive(true);

            if (done)
            {
                text.color = completedTextColor;
                image.color = color;
            }
            else
            {
                text.color = notCompletedTextColor;
                image.color = notCompletedColor;
            }
        }
    }

    public void OnClick()
    {
        GameManager.Instance().levelToPlay = levelNumber - 1;
        GameManager.Instance().LoadNewLevel();
    }
}
