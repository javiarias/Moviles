using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class LevelButton : MonoBehaviour
{
    // Componentes del botón como tal
    public Image _image;
    public Image _lockImage;
    public Button _button;
    public Text _text;

    int _levelNumber;
    int _pack;

    //Color
    private Color notCompletedColor = new Color(255, 255, 255);
    private Color notCompletedTextColor = new Color(0, 0, 0);
    private Color completedTextColor = new Color(255, 255, 255);
    private Color blockedColor = new Color(255, 255, 255);
    private Color blockedLockColor = new Color(0,0,0);

    private bool _available;

    public void setUp(bool available, int i, Color color, int pack_, bool done)
    {
        _pack = pack_;
        _levelNumber = i;
        _available = available;

        _image.gameObject.SetActive(true);

        if (!_available)
        {
            _image.color = blockedColor;
            _lockImage.color = blockedLockColor;
            _lockImage.gameObject.SetActive(true);
        }
        else
        {
            _text.text = i.ToString();
            _text.gameObject.SetActive(true);

            if (done)
            {
                _text.color = completedTextColor;
                _image.color = color;
            }
            else
            {
                _text.color = notCompletedTextColor;
                _image.color = notCompletedColor;
            }
        }
    }

    public void OnClick()
    {
        if (_available)
        {
            GameManager.Instance().StartGame(_pack, _levelNumber - 1);
        }
    }
}
