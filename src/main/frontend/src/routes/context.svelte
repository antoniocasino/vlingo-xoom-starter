<script>
	import { TextField } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, setLocalStorage } from '../stores';
	import { artifactRule, packageRule, requireRule, versionRule, xoomVersionRule } from '../validators';
	
	let groupId = $contextSettings ? $contextSettings.groupId : "";
	let artifactId = $contextSettings ? $contextSettings.artifactId : "";
	let artifactVersion = $contextSettings ? $contextSettings.artifactVersion : "";
	let packageName = $contextSettings ? $contextSettings.packageName : "";
	let xoomVersion = "1.4.1-SNAPSHOT";

	$: valid = !packageRule(groupId) && !artifactRule(artifactId) && !versionRule(artifactVersion) && !packageRule(packageName) && !xoomVersionRule(xoomVersion);
	$: if(valid) {
		$contextSettings = { groupId, artifactId, artifactVersion, packageName, xoomVersion };
		setLocalStorage("contextSettings", $contextSettings)
	}
</script>

<svelte:head>
	<title>Context</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Context" next="aggregates" bind:valid>
	<TextField class="mb-4 pb-4" placeholder="com.example" bind:value={groupId} rules={[requireRule, packageRule]} validateOnBlur={!groupId}>Group Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="demo" bind:value={artifactId} rules={[requireRule, artifactRule]} validateOnBlur={!artifactId}>Artifact Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="1.0.0" bind:value={artifactVersion} rules={[requireRule, versionRule]} validateOnBlur={!artifactVersion}>Artifact Version</TextField>
	<TextField class="mb-4 pb-4" placeholder="com.example.demo" bind:value={packageName} rules={[requireRule, packageRule]} validateOnBlur={!packageName}>Base Package Name</TextField>
</CardForm>