// MultiCaptureTestDlg.h : �w�b�_�[ �t�@�C��
//

#pragma once


// CMultiCaptureTestDlg �_�C�A���O
class CMultiCaptureTestDlg : public CDialog
{
// �R���X�g���N�V����
public:
	CMultiCaptureTestDlg(CWnd* pParent = NULL);	// �W���R���X�g���N�^

// �_�C�A���O �f�[�^
	enum { IDD = IDD_MULTICAPTURETEST_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV �T�|�[�g


// ����
protected:
	HICON m_hIcon;

	// �������ꂽ�A���b�Z�[�W���蓖�Ċ֐�
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg LRESULT OnASyncCapture(UINT wParam,LONG lParam);
	afx_msg void OnBnClickedSwitch();
	afx_msg void OnClose();
	afx_msg void OnDestroy();
	afx_msg void OnTimer(UINT_PTR nIDEvent);
private:
	bool is_start;
};