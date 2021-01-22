using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class UpdateHint : MonoBehaviour
{
    public Text _hintTxt;

    private void Start()
    {
        if (_hintTxt)
            _hintTxt.text = (GameManager.Instance().GetHints()).ToString();
    }

    public void UpdateTxt()
    {
        if (_hintTxt)
            _hintTxt.text = (GameManager.Instance().GetHints() + 1).ToString();
    }
}
